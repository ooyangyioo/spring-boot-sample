package org.yangyi.project.config;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yangyi.project.shiro.*;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * shiro配置类
 */
@Configuration
public class ShiroConfig {

    private static final String LOGIN_URL = "/user/login";

    private IgnorePathConfig ignorePathConfig;

    /**
     * 创建自定义Redis缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {
        return new RedisCacheManager();
    }

    @Autowired
    public void setIgnorePathConfig(IgnorePathConfig ignorePathConfig) {
        this.ignorePathConfig = ignorePathConfig;
    }

    /**
     * 注册shiro的Filter，拦截请求
     */
    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(SecurityManager securityManager) throws Exception {
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(shiroFilterFactoryBean(securityManager).getObject());
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return filterRegistration;
    }

    @Bean
    public Authenticator authenticator() {
        CustomModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(jwtAuthorizingRealm(), userAuthorizingRealm()));
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }

    /**
     * 禁用session
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator() {
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    /**
     * 用于用户名密码登录时认证
     */
    @Bean
    public Realm userAuthorizingRealm() {
        UserAuthorizingRealm userAuthorizingRealm = new UserAuthorizingRealm();
        //  是否开启缓存
        userAuthorizingRealm.setCachingEnabled(false);
        //  启用身份验证缓存，及缓存AuthenticationInfo信息
        userAuthorizingRealm.setAuthenticationCachingEnabled(false);
        //  启用授权缓存，即缓存AuthorizationInfo信息
        userAuthorizingRealm.setAuthorizationCachingEnabled(false);
        return userAuthorizingRealm;
    }

    /**
     * 用于Jwt Token认证
     */
    @Bean
    public Realm jwtAuthorizingRealm() {
        JwtAuthorizingRealm jwtAuthorizingRealm = new JwtAuthorizingRealm();
        jwtAuthorizingRealm.setCachingEnabled(true);
        return jwtAuthorizingRealm;
    }

    /**
     * 设置过滤器，将自定义的Filter加入
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        DefaultWebSecurityManager defaultWebSecurityManager = (DefaultWebSecurityManager) securityManager;

        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setLoginUrl(LOGIN_URL);

        Map<String, Filter> filterMap = factoryBean.getFilters();
        filterMap.put("jwt", new JwtAuthenticatingFilter());
        filterMap.put("anyRole", new AnyRolesAuthorizationFilter());
        factoryBean.setFilters(filterMap);

        factoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return factoryBean;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> pathDefinitions = new HashMap<>();
        for (String path : ignorePathConfig.getPaths())
            pathDefinitions.put(path, "anon");
        chainDefinition.addPathDefinitions(pathDefinitions);
        /*
        设置登陆地址不需要权限认证
        此处也可以不设置，通过 factoryBean.setLoginUrl(LOGIN_URL);
        然后在JwtAuthenticatingFilter 中进行处理
         */
        chainDefinition.addPathDefinition(LOGIN_URL, "anon");
        chainDefinition.addPathDefinition("/user/info", "jwt,anyRole[admin]");
        //  permissive 即使登录认证没通过，因为isPermissive返回true，还是会让请求继续下去的
        chainDefinition.addPathDefinition("/user/logout", "jwt[permissive]");
        chainDefinition.addPathDefinition("/**", "jwt");
        return chainDefinition;
    }

    /**
     * 自动创建代理
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * Shiro生命周期处理器
     * 此配置类注入bean和使用@Value注解需要将此方法改为static
     */
    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro aop注解支持.否则@RequiresRoles等注解无法生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
