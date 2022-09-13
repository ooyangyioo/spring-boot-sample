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

        ShiroAuthenticationFilter shiroAuthenticationFilter = new ShiroAuthenticationFilter();  //  自定义登录
        shiroAuthenticationFilter.setRememberMeParam(REMEMBER_ME_PARAM);
        shiroAuthenticationFilter.setUsernameParam(USERNAME_PARAM);
        shiroAuthenticationFilter.setPasswordParam(PASSWORD_PARAM);

        ShiroUserFilter shiroUserFilter = new ShiroUserFilter();    //  自定义rememberMe过滤器

        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter(); //  限制登录人数
        kickoutSessionFilter.setSessionManager(sessionManager());
        kickoutSessionFilter.setCacheManager(redisCacheManager());

        filters.put("auth", shiroAuthenticationFilter);
        filters.put("logout", new ShiroLogoutFilter()); //  自定义登出
        filters.put("user", shiroUserFilter);
        filters.put("kickout", kickoutSessionFilter);

        filterFactoryBean.setFilters(filters);
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        return filterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        //  设置Realm
        webSecurityManager.setRealm(shiroRealm());
        //  webSecurityManager.setCacheManager(ehCacheManager()); //  使用EhCache作为缓存管理器
        webSecurityManager.setCacheManager(redisCacheManager()); //  使用Redis作为缓存管理器
        webSecurityManager.setRememberMeManager(rememberMeManager());   //  记住我
        webSecurityManager.setSessionManager(sessionManager()); //  配置自定义session管理，使用Redis
        return webSecurityManager;
    }

    /**
     * 自定义Realm
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(credentialsMatcher());
        shiroRealm.setCachingEnabled(true); //  开启缓存
        shiroRealm.setAuthenticationCachingEnabled(true); //    开启认证缓存
        shiroRealm.setAuthenticationCacheName(AUTHENTICATION_CACHE_NAME);
        shiroRealm.setAuthorizationCachingEnabled(true);    //  开启授权缓存
        shiroRealm.setAuthorizationCacheName(AUTHORIZATION_CACHE_NAME);
        return shiroRealm;
    }

    /**
     * EhCache 缓存管理器
     */
    public CacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
        return cacheManager;
    }

    public CacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setPrincipalIdFieldName("username");  //  针对不同用户缓存
        redisCacheManager.setExpire(3600);    //  用户权限信息缓存时间
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
     * RememberMe管理器
     */
    @Bean
    public RememberMeManager rememberMeManager() {
        // 设置cookie名称，需要前端传值与cookie名称对应
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
        sessionManager.setGlobalSessionTimeout(60000);    //  会话超时时间，单位：毫秒，默认30分钟(1800000)
        sessionManager.setDeleteInvalidSessions(true);  //  是否开启删除无效的session对象 默认true
        sessionManager.setSessionValidationSchedulerEnabled(true);  //  是否开启定时调度器进行检测过期session 默认为 true
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(60000);
        sessionManager.setSessionIdUrlRewritingEnabled(false);  //  取消url 后面的sessionId

        sessionManager.setSessionIdCookie(sessionIdCookie); // 指定sessionId

        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.getSessionIdCookie().setSecure(false);
        sessionManager.setSessionDAO(redisSessionDAO());
        //  配置监听
        sessionManager.setSessionListeners(listeners);

        return sessionManager;
    }

    @Bean
    public HashedCredentialsMatcher credentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(redisCacheManager());
        credentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);  // 散列算法，SHA256算法
        //  credentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);  // 散列算法，MD5算法
        credentialsMatcher.setStoredCredentialsHexEncoded(false);  // 数据库存储的密码字段使用HEX还是BASE64方式加密
        credentialsMatcher.setHashIterations(1024);  // 散列迭代次数
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
         * 直接在登录过滤器中处理，不需要在controller中定义登录处理
         * kickout限制登陆人数，该过滤器一定要在第一个
         */
        chainDefinition.addPathDefinition("/sys/user/login", "kickout,auth");
        //  不需要权限可以访问，需要在controller中定义登录处理，需要修改登录过滤器为 ShiroAuthenticationFilterWithController
        //  chainDefinition.addPathDefinition("/user/login", "anon");
        chainDefinition.addPathDefinition("/sys/user/logout", "logout");
        chainDefinition.addPathDefinition("/sys/user/signup", "anon");
        chainDefinition.addPathDefinition("/kaptcha/create", "anon");
        chainDefinition.addPathDefinition("/kaptcha/check", "anon");
        //  permissive 即使不登录，因为isPermissive返回true，还是会让请求继续下去。
        //  chainDefinition.addPathDefinition("/info", "auth[permissive]");

        //  authc：表示需要认证才能进行访问; user：表示配置记住我或认证通过可以访问的地址
        chainDefinition.addPathDefinition("/**", "user");
        return chainDefinition;
    }

    /**
     * 开启Shiro aop注解支持
     * 否则@RequiresRoles等注解无法生效
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
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
     * 让某个实力的某个方法的返回值注入为Bean的实力
     * Spring静态注入
     */
    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(new Object[]{securityManager()});
        return factoryBean;
    }

}
