package org.yangyi.project.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yangyi.project.mapper.SysUserMapper;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录失败次数限制
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryLimitHashedCredentialsMatcher.class);

    private Cache<String, AtomicInteger> cache;

    @Autowired
    private SysUserMapper sysUserMapper;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        cache = cacheManager.getCache("retry");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        // 获取用户登录次数
        AtomicInteger retryCount = cache.get(username);
        // 如果用户没有登陆过,登陆次数加1 并放入缓存
        if (retryCount == null)
            retryCount = new AtomicInteger(0);

        if (retryCount.incrementAndGet() > 3) {
            sysUserMapper.userLockStatus(username, 1);
            throw new LockedAccountException("用户被锁定！");
        }

        boolean matches = super.doCredentialsMatch(token, info);
        if (matches)
            cache.remove(username);
        else
            cache.put(username, retryCount);
        return matches;
    }

}
