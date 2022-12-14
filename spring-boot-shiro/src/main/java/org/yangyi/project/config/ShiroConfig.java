package org.yangyi.project.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.yangyi.project.shiro.*;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    private static final String LOGIN_URL = "/sys/user/login";

    private IgnorePathConfig ignorePathConfig;

    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String REMEMBER_ME_PARAM = "remember";
    public static final String AUTHENTICATION_CACHE_NAME = "authentication";
    public static final String AUTHORIZATION_CACHE_NAME = "authorization";

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public void setIgnorePathConfig(IgnorePathConfig ignorePathConfig) {
        this.ignorePathConfig = ignorePathConfig;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setLoginUrl(LOGIN_URL);

        Map<String, Filter> filters = filterFactoryBean.getFilters();

        ShiroAuthenticationFilter shiroAuthenticationFilter = new ShiroAuthenticationFilter();  //  ???????????????
        shiroAuthenticationFilter.setRememberMeParam(REMEMBER_ME_PARAM);
        shiroAuthenticationFilter.setUsernameParam(USERNAME_PARAM);
        shiroAuthenticationFilter.setPasswordParam(PASSWORD_PARAM);

        ShiroUserFilter shiroUserFilter = new ShiroUserFilter();    //  ?????????rememberMe?????????

        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter(); //  ??????????????????
        kickoutSessionFilter.setSessionManager(sessionManager());
        kickoutSessionFilter.setCacheManager(redisCacheManager());

        filters.put("auth", shiroAuthenticationFilter);
        filters.put("logout", new ShiroLogoutFilter()); //  ???????????????
        filters.put("user", shiroUserFilter);
        filters.put("kickout", kickoutSessionFilter);

        filterFactoryBean.setFilters(filters);
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return filterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        //  ??????Realm
        webSecurityManager.setRealm(shiroRealm());
        //  webSecurityManager.setCacheManager(ehCacheManager()); //  ??????EhCache?????????????????????
        webSecurityManager.setCacheManager(redisCacheManager()); //  ??????Redis?????????????????????
        webSecurityManager.setRememberMeManager(rememberMeManager());   //  ?????????
        webSecurityManager.setSessionManager(sessionManager()); //  ???????????????session???????????????Redis
        return webSecurityManager;
    }

    /**
     * ?????????Realm
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
        shiroRealm.setCachingEnabled(true); //  ????????????
        shiroRealm.setAuthenticationCachingEnabled(true); //    ??????????????????
        shiroRealm.setAuthenticationCacheName(AUTHENTICATION_CACHE_NAME);
        shiroRealm.setAuthorizationCachingEnabled(true);    //  ??????????????????
        shiroRealm.setAuthorizationCacheName(AUTHORIZATION_CACHE_NAME);
        return shiroRealm;
    }

    /**
     * EhCache ???????????????
     */
    public CacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
        return cacheManager;
    }

    public CacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setPrincipalIdFieldName("username");  //  ????????????????????????
        redisCacheManager.setExpire(3600);    //  ??????????????????????????????
        return redisCacheManager;
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
        //  redisSessionDAO.setSessionIdGenerator(new RandomSessionIdGenerator());
        redisSessionDAO.setExpire(1800);
        return redisSessionDAO;
    }

    @Bean
    public RedisManager redisManager() {
//        redisConnectionFactory.getConnection().set("test".getBytes(), "HelloWorld".getBytes());
//        byte[] data = redisConnectionFactory.getConnection().get("test".getBytes());
//        System.err.println(new String(data));

        return new RedisManager();
    }

    /**
     * RememberMe?????????
     */
    @Bean
    public RememberMeManager rememberMeManager() {
        // ??????cookie??????????????????????????????cookie????????????
        SimpleCookie rememberMeCookie = new SimpleCookie(REMEMBER_ME_PARAM);
        rememberMeCookie.setHttpOnly(true);
        rememberMeCookie.setMaxAge(24 * 60 * 60);

        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie);
        Base64.encodeToString("yangyi19910126-+".getBytes());
        Base64.decodeToString("eWFuZ3lpMTk5MTAxMjYtKw==");
        rememberMeManager.setCipherKey(Base64.decode("eWFuZ3lpMTk5MTAxMjYtKw=="));
        return rememberMeManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        SimpleCookie sessionIdCookie = new SimpleCookie("sid");
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setMaxAge(-1);

        Collection<SessionListener> listeners = new ArrayList<>();
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        listeners.add(sessionListener);

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(60000);    //  ?????????????????????????????????????????????30??????(1800000)
        sessionManager.setDeleteInvalidSessions(true);  //  ???????????????????????????session?????? ??????true
        sessionManager.setSessionValidationSchedulerEnabled(true);  //  ?????????????????????????????????????????????session ????????? true
        //??????session?????????????????????, ?????????????????????????????????????????????????????? ????????? 1?????????
        //??????????????? ?????????????????? ExecutorServiceSessionValidationScheduler ??????????????????????????????ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(60000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);  //  ??????url ?????????sessionId

        sessionManager.setSessionIdCookie(sessionIdCookie); // ??????sessionId

        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.getSessionIdCookie().setSecure(false);
        sessionManager.setSessionDAO(redisSessionDAO());
        //  ????????????
        sessionManager.setSessionListeners(listeners);

        return sessionManager;
    }

    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(redisCacheManager());
        credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);  // ???????????????SHA256??????
        //  credentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);  // ???????????????MD5??????
        credentialsMatcher.setStoredCredentialsHexEncoded(false);  // ????????????????????????????????????HEX??????BASE64????????????
        credentialsMatcher.setHashIterations(1024);  // ??????????????????
        return credentialsMatcher;
    }

    @Bean
    protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String, String> pathDefinitions = new HashMap<>();
        for (String path : ignorePathConfig.getPaths())
            pathDefinitions.put(path, "anon");
        chainDefinition.addPathDefinitions(pathDefinitions);

        /**
         * ????????????????????????????????????????????????controller?????????????????????
         * kickout??????????????????????????????????????????????????????
         */
        chainDefinition.addPathDefinition("/sys/user/login", "kickout,auth");
        //  ???????????????????????????????????????controller?????????????????????????????????????????????????????? ShiroAuthenticationFilterWithController
        //  chainDefinition.addPathDefinition("/user/login", "anon");
        chainDefinition.addPathDefinition("/sys/user/logout", "logout");
        chainDefinition.addPathDefinition("/sys/user/signup", "anon");
        chainDefinition.addPathDefinition("/kaptcha/create", "anon");
        chainDefinition.addPathDefinition("/kaptcha/check", "anon");
        //  permissive ????????????????????????isPermissive??????true????????????????????????????????????
        //  chainDefinition.addPathDefinition("/info", "auth[permissive]");

        //  authc???????????????????????????????????????; user????????????????????????????????????????????????????????????
        chainDefinition.addPathDefinition("/**", "user");
        return chainDefinition;
    }

    /**
     * ??????Shiro aop????????????
     * ??????@RequiresRoles?????????????????????
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * ??????????????????
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * ???????????????????????????????????????????????????Bean?????????
     * Spring????????????
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }

}
