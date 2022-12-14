package org.yangyi.project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.yangyi.project.component.*;
import org.yangyi.project.filter.JwtAuthorizationFilter;
import org.yangyi.project.filter.JwtAuthenticationFilter;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfigurerImpl.class);

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final JwtLoginFailureHandler jwtLoginFailureHandler;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtFilterInvocationSecurityMetadataSource jwtFilterInvocationSecurityMetadataSource;
    private final JwtAccessDecisionManager jwtAccessDecisionManager;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final JwtSessionInformationExpiredStrategy jwtSessionInformationExpiredStrategy;

    public WebSecurityConfigurerImpl(JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                     JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                     JwtLoginSuccessHandler jwtLoginSuccessHandler,
                                     JwtLoginFailureHandler jwtLoginFailureHandler,
                                     JwtAuthenticationProvider jwtAuthenticationProvider,
                                     JwtFilterInvocationSecurityMetadataSource jwtFilterInvocationSecurityMetadataSource,
                                     JwtAccessDecisionManager jwtAccessDecisionManager,
                                     JwtLogoutSuccessHandler jwtLogoutSuccessHandler,
                                     JwtSessionInformationExpiredStrategy jwtSessionInformationExpiredStrategy) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtLoginSuccessHandler = jwtLoginSuccessHandler;
        this.jwtLoginFailureHandler = jwtLoginFailureHandler;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.jwtFilterInvocationSecurityMetadataSource = jwtFilterInvocationSecurityMetadataSource;
        this.jwtAccessDecisionManager = jwtAccessDecisionManager;
        this.jwtLogoutSuccessHandler = jwtLogoutSuccessHandler;
        this.jwtSessionInformationExpiredStrategy = jwtSessionInformationExpiredStrategy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //  ????????????????????????
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/jwt/login"); //  ???????????????????????????
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler); //  ?????????????????????
        jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtLoginFailureHandler); //  ?????????????????????

        //  ?????????JWT???????????????
        JwtAuthorizationFilter JWTAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(), (request, response, authException) -> {
            LOGGER.info("Token ???????????????{}", authException.getMessage());
            ResponseUtil.unauthorizedResponse(response, new ResponseVO("0", authException.getMessage()));
        });

        http.formLogin().disable(). //  ?????? UsernamePasswordAuthenticationFilter ?????????
                httpBasic().disable().  //  ??????  BasicAuthenticationFilter ?????????
                authorizeRequests()
                //  ??????????????????????????????????????????????????? FilterSecurityInterceptor ???????????? permitAll() ????????????
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(jwtFilterInvocationSecurityMetadataSource); //  ????????????
                        object.setAccessDecisionManager(jwtAccessDecisionManager); //  ???????????????
                        return object;
                    }
                })
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/user/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   //  ???????????????UsernamePasswordAuthenticationFilter???????????? Filter
                .addFilterAfter(JWTAuthorizationFilter, JwtAuthenticationFilter.class)   //  ??????JWT filter?????????????????????Token????????????
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //  ??????session
//                .sessionManagement().maximumSessions(1).  //  ???????????????????????????????????????
//                expiredSessionStrategy(jwtSessionInformationExpiredStrategy)  //  ????????????????????????
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint). //  ????????????????????????????????????????????????
                accessDeniedHandler(jwtAccessDeniedHandler)   //  ??????????????????????????????????????????????????????
                .and()
                .cors().and().csrf().disable();

        /**
         * ????????????
         */
//        http.logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(jwtLogoutSuccessHandler) //  ????????????????????????
//                .deleteCookies("JSESSIONID", "SESSION");   //  ??????????????????cookie
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // ??????????????????????????????
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/*");
        web.ignoring().antMatchers("/favicon.ico");
    }

}
