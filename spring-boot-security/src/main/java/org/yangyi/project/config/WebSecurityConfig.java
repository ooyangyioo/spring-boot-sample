package org.yangyi.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.yangyi.project.security.*;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;


/**
 * prePostEnabled属性决定Spring Security在接口前注解是否可用@PreAuthorize,@PostAuthorize等注解,设置为true,会拦截加了这些注解的接口
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationProviderImpl authenticationProvider;
    private PermissionEvaluatorImpl permissionEvaluator;
    private AccessDecisionManagerImpl accessDecisionManager;
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;
    private IgnorePathConfig ignorePathConfig;

    @Autowired
    public void setAuthenticationProvider(AuthenticationProviderImpl authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Autowired
    public void setPermissionEvaluator(PermissionEvaluatorImpl permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    @Autowired
    public void setAccessDecisionManager(AccessDecisionManagerImpl accessDecisionManager) {
        this.accessDecisionManager = accessDecisionManager;
    }

    @Autowired
    public void setFilterInvocationSecurityMetadataSource(FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource) {
        this.filterInvocationSecurityMetadataSource = filterInvocationSecurityMetadataSource;
    }

    @Autowired
    public void setIgnorePathConfig(IgnorePathConfig ignorePathConfig) {
        this.ignorePathConfig = ignorePathConfig;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(permissionEvaluator);
        return handler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //  auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        auth.authenticationProvider(authenticationProvider);
        super.configure(auth);
    }

//    @Bean
//    GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        // 去除 ROLE_ 前缀
//        return new GrantedAuthorityDefaults("");
//    }

    /**
     * http相关的配置，包括登入登出、异常处理、会话管理等
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();

        http.formLogin()
                .usernameParameter("username")  //  设置登录账号参数，与表单参数一致
                .passwordParameter("password")  //  设置登录密码参数，与表单参数一致
                .loginProcessingUrl("/user/login")
                .permitAll();

        http.formLogin()
                .successHandler((request, response, authentication) -> {
                    ResponseUtil.okResponse(response, new ResponseVO("1", "成功", null));
                })  //  登录成功处理器
                .failureHandler((request, response, exception) -> {
                    ResponseUtil.okResponse(response, new ResponseVO("0", exception.getMessage(), null));
                });  //  登录失败处理器

        // 自定义json请求参数登录
//        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter(authenticationManager());
//        jsonAuthenticationFilter.setFilterProcessesUrl("/user/login"); //  设置过滤器拦截地址
//        jsonAuthenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
//            ResponseUtil.okResponse(response, new ResponseVO("1", "成功", null));
//        }); //  成功结果处理器
//        jsonAuthenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
//            ResponseUtil.okResponse(response, new ResponseVO("0", exception.getMessage(), null));
//        }); //  失败结果处理器
//        http.formLogin().disable(). //  禁用 UsernamePasswordAuthenticationFilter 过滤器
//                httpBasic().disable().  //  禁用  BasicAuthenticationFilter 过滤器
//                addFilterAt(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        /**
         * 自定义动态权限
         */
//        http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//            @Override
//            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
//                object.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); //  安全元数据源
//                object.setAccessDecisionManager(accessDecisionManager); //  决策管理器
//                return object;
//            }
//        });

        http.logout()
                .logoutUrl("/user/logout")
                .clearAuthentication(true)  //  清除身份认证信息
                .invalidateHttpSession(true)    //  使session失效
                .logoutSuccessHandler((request, response, authentication) -> {
                    ResponseUtil.okResponse(response, new ResponseVO("1", "成功", null));
                }) //  登出成功处理
                .deleteCookies("SESSION");   //  登出之后删除cookie

        http.exceptionHandling().
                authenticationEntryPoint((request, response, authException) -> {
                    ResponseUtil.unauthorizedResponse(response, new ResponseVO("0", "请登录！", null));
                }) //  匿名用户访问无权限资源时的异常处理
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    ResponseUtil.forbiddenResponse(response, new ResponseVO("0", "无访问权限！", null));
                });   //  没有权限处理

        http.sessionManagement()
                .invalidSessionStrategy((request, response) -> {
                    ResponseUtil.unauthorizedResponse(response, new ResponseVO("0", "会话过期，请重新登录！", null));
                })  // session 超时或不存在的处理器
                .maximumSessions(1)    //  同一账号同时登录最大用户数
                .maxSessionsPreventsLogin(false)    //  是否保留已经登录的用户；true，新用户无法登录；false，旧用户被踢出
                .expiredSessionStrategy(event -> {
                    ResponseUtil.unauthorizedResponse(event.getResponse(), new ResponseVO("0", "账号在别处登录！", null));
                });  //  会话失效处理逻辑

        http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        //  不需要授权的请求地址
        http.authorizeRequests().antMatchers(ignorePathConfig.getPaths()).permitAll();
        http.authorizeRequests().anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) {
        //  忽略地址
        //  web.ignoring().antMatchers(ignorePathConfig.getPaths());
    }

}
