package org.yangyi.project.security;

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
import org.yangyi.project.web.ResponseUtil;
import org.yangyi.project.web.ResponseVO;

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

        //  自定义登录拦截器
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/jwt/login"); //  设置过滤器拦截地址
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler); //  成功结果处理器
        jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtLoginFailureHandler); //  失败结果处理器

        //  自定义JWT校验拦截器
        JwtAuthorizationFilter JWTAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager(), (request, response, authException) -> {
            LOGGER.info("Token 验证失败：{}", authException.getMessage());
            ResponseUtil.unauthorizedResponse(response, ResponseVO.failed(authException.getMessage()));
        });

        http.formLogin().disable(). //  禁用 UsernamePasswordAuthenticationFilter 过滤器
                httpBasic().disable().  //  禁用  BasicAuthenticationFilter 过滤器
                authorizeRequests()
                //  自定义动态权限，该配置会覆盖默认的 FilterSecurityInterceptor 从而导致 permitAll() 配置失效
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(jwtFilterInvocationSecurityMetadataSource); //  权限数据
                        object.setAccessDecisionManager(jwtAccessDecisionManager); //  决策管理器
                        return object;
                    }
                })
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/user/test").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   //  加入自定义UsernamePasswordAuthenticationFilter替代原有 Filter
                .addFilterAfter(JWTAuthorizationFilter, JwtAuthenticationFilter.class)   //  添加JWT filter验证其他请求的Token是否合法
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //  禁用session
//                .sessionManagement().maximumSessions(1).  //  同一账号同时登录最大用户数
//                expiredSessionStrategy(jwtSessionInformationExpiredStrategy)  //  会话信息过期策略
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint). //  匿名用户访问无权限资源的异常处理
                accessDeniedHandler(jwtAccessDeniedHandler)   //  认证过的用户访问无权限资源的异常处理
                .and()
                .cors().and().csrf().disable();

        /**
         * 登出设置
         */
//        http.logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(jwtLogoutSuccessHandler) //  登出成功处理逻辑
//                .deleteCookies("JSESSIONID", "SESSION");   //  登出之后删除cookie
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义用户验证器
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/*");
        web.ignoring().antMatchers("/favicon.ico");
    }

}
