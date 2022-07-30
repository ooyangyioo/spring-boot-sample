package org.yangyi.project.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.yangyi.project.entity.SysMenu;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionEvaluatorImpl.class);
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        LOGGER.info("请求：[{}]，需要权限：[{}]", targetDomainObject, permission);
        User user = (User) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            List<SysMenu> sysMenus = ((UrlGrantedAuthority) authority).getSysMenus();
            for (SysMenu sysMenu : sysMenus) {
                if (antPathMatcher.match(sysMenu.getMenuUrl(), (String) targetDomainObject) &&
                        (StringUtils.equals(sysMenu.getMenuTag(), (String) permission) || StringUtils.equals(sysMenu.getMenuTag(), "*")))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

}
