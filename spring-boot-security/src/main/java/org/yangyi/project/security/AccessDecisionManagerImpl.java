package org.yangyi.project.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 访问决策管理器
 */
@Component
public class AccessDecisionManagerImpl implements AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDecisionManagerImpl.class);

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        LOGGER.info("当前Authentication类型：{}", authentication);
        for (Iterator<ConfigAttribute> iterator = collection.iterator(); iterator.hasNext(); ) {
            //  当前请求需要的权限
            String needRole = iterator.next().getAttribute();
            //  当前用户所具有的权限
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals(needRole.trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
