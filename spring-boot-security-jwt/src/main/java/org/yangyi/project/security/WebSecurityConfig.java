package org.yangyi.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.yangyi.project.web.ApiResponseUtil;
import org.yangyi.project.web.ApiResponseVO;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
    private final JwtLoginFailureHandler jwtLoginFailureHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtFilterInvocationSecurityMetadataSource jwtFilterInvocationSecurityMetadataSource;
    private final JwtAccessDecisionManager jwtAccessDecisionManager;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final JwtSessionInformationExpiredStrategy jwtSessionInformationExpiredStrategy;

    @Autowired
    private PermissionEvaluatorImpl permissionEvaluator;

    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    public WebSecurityConfig(JwtAccessDeniedHandler jwtAccessDeniedHandler,
                             JwtLoginSuccessHandler jwtLoginSuccessHandler,
                             JwtLoginFailureHandler jwtLoginFailureHandler,
                             JwtAuthenticationProvider jwtAuthenticationProvider,
                             JwtFilterInvocationSecurityMetadataSource jwtFilterInvocationSecurityMetadataSource,
                             JwtAccessDecisionManager jwtAccessDecisionManager,
                             JwtLogoutSuccessHandler jwtLogoutSuccessHandler,
                             JwtSessionInformationExpiredStrategy jwtSessionInformationExpiredStrategy) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
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
        jwtAuthenticationFilter.setPostOnly(true);
        jwtAuthenticationFilter.setFilterProcessesUrl("/user/login"); //  设置过滤器拦截地址
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtLoginSuccessHandler); //  成功结果处理器
        jwtAuthenticationFilter.setAuthenticationFailureHandler(jwtLoginFailureHandler); //  失败结果处理器

        http.formLogin().disable(). //  禁用 UsernamePasswordAuthenticationFilter 过滤器
                httpBasic().disable().  //  禁用  BasicAuthenticationFilter 过滤器
                authorizeRequests()
                //  自定义动态权限，该配置会覆盖默认的 FilterSecurityInterceptor 从而导致 permitAll() 配置失效
//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                        object.setSecurityMetadataSource(jwtFilterInvocationSecurityMetadataSource); //  权限数据
//                        object.setAccessDecisionManager(jwtAccessDecisionManager); //  决策管理器
//                        return object;
//                    }
//                })
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/user/signup").permitAll()
                .anyRequest().authenticated()
                .and().addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)   //  加入自定义UsernamePasswordAuthenticationFilter替代原有 Filter
                .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class)   //  添加JWT filter验证其他请求的Token是否合法
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //  禁用session
//                .sessionManagement().maximumSessions(1).  //  同一账号同时登录最大用户数
//                expiredSessionStrategy(jwtSessionInformationExpiredStrategy)  //  会话信息过期策略
                .and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> ApiResponseUtil.unauthorizedResponse(response, ApiResponseVO.failed("请登录"))) //  用户访问无权限资源的异常处理
                .accessDeniedHandler(jwtAccessDeniedHandler)   //  认证过的用户访问无权限资源的异常处理
                .and().cors().and().csrf().disable();

        http.authorizeRequests().expressionHandler(webSecurityExpressionHandler());

        /**
         * 登出设置
         */
        http.logout()
                .logoutUrl("/user/logout")
                .logoutSuccessHandler(jwtLogoutSuccessHandler); //  登出成功处理逻辑
//                .deleteCookies("JSESSIONID", "SESSION");   //  登出之后删除cookie
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义用户验证器
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers("/static/**");
        webSecurity.ignoring().antMatchers("/favicon.ico");

        /**
         * 去除SpringSecurity中默认的权限ROLE_前缀
         * ROLE_前缀需要数据库中就存在或者查询用户的时候加上，否则在使用hasRole的时候无法识别
         * 或者使用 {@link MethodSecurityConfig#grantedAuthorityDefaults()}
         */
//        webSecurity.expressionHandler(new DefaultWebSecurityExpressionHandler() {
//            @Override
//            protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
//                WebSecurityExpressionRoot root = (WebSecurityExpressionRoot) super.createSecurityExpressionRoot(authentication, fi);
//                // 去除默认的ROLE_前缀
//                root.setDefaultRolePrefix("");
//                return root;
//            }
//        });
    }

    @Bean(name = "myWebSecurityExpressionHandler")
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        return webSecurityExpressionHandler;
    }

}
