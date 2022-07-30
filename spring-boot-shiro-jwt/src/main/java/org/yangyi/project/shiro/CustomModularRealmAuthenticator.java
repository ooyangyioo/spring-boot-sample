package org.yangyi.project.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authc.pam.ShortCircuitIterationException;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 自定义重写ModularRealmAuthenticator类，用于处理多个Realm的自定义异常捕获问题
 */
public class CustomModularRealmAuthenticator extends ModularRealmAuthenticator {

    private static final Logger log = LoggerFactory.getLogger(CustomModularRealmAuthenticator.class);

    /**
     * 取消执行代码 realm.getAuthenticationInfo(token); 的try catch
     */
    @Override
    protected AuthenticationInfo doMultiRealmAuthentication(Collection<Realm> realms, AuthenticationToken token) {
        AuthenticationStrategy strategy = getAuthenticationStrategy();
        AuthenticationInfo aggregate = strategy.beforeAllAttempts(realms, token);
        if (log.isTraceEnabled()) {
            log.trace("Iterating through {} realms for PAM authentication", realms.size());
        }
        for (Realm realm : realms) {
            try {
                aggregate = strategy.beforeAttempt(realm, token, aggregate);
            } catch (ShortCircuitIterationException shortCircuitSignal) {
                // Break from continuing with subsequnet realms on receiving
                // short circuit signal from strategy
                break;
            }

            if (realm.supports(token)) {
                log.trace("Attempting to authenticate token [{}] using realm [{}]", token, realm);
                AuthenticationInfo info = null;
                Throwable t = null;
                info = realm.getAuthenticationInfo(token);
                aggregate = strategy.afterAttempt(realm, token, info, aggregate, t);
            } else {
                log.debug("Realm [{}] does not support token {}.  Skipping realm.", realm, token);
            }

        }

        aggregate = strategy.afterAllAttempts(token, aggregate);

        return aggregate;
    }

}
