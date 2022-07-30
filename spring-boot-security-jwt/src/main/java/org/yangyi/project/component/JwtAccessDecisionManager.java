package org.yangyi.project.component;

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

@Component
public class JwtAccessDecisionManager implements AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        LOGGER.info("接口需要的权限：{}，用户拥有的权限：{}", configAttributes, authentication.getAuthorities());
        for (Iterator<ConfigAttribute> iterator = configAttributes.iterator(); iterator.hasNext(); ) {
            String needRole = iterator.next().getAttribute();
            Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
            for (GrantedAuthority authority : grantedAuthorities) {
                if (authority.getAuthority().equals(needRole.trim())) {
                    return;
                }
            }
        }

        throw new AccessDeniedException("无权限访问!");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        System.err.println("55");
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        System.err.println("66--->" + clazz);
        return true;
    }

}
