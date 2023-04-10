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
import org.yangyi.project.system.po.SysMenu;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * hasPermission()的注解会委托给PermissionEvaluator接口，而这个接口默认实现是DenyAllPermissionEvaluator
 * 这个类的两个方法直接返回了false，所以我们必须自己实现一个PermissionEvaluator 并且把它注入到默认DefaultWebSecurityExpressionHandler类中
 * （注意与DefaultMethodSecurityExpressionHandler区分，这两个类都持有permissionEvaluator 应该一个是web环境一个是SE环境,我这里是web环境，故而是DefaultWebSecurityExpressionHandler类）
 */
@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionEvaluatorImpl.class);
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        LOGGER.info("请求：[{}]，需要权限：[{}]", targetDomainObject, permission);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
//            List<SysMenu> sysMenus = ((UrlGrantedAuthority) authority).getSysMenus();
//            for (SysMenu sysMenu : sysMenus) {
//                if (antPathMatcher.match(sysMenu.getMenuUrl(), (String) targetDomainObject) &&
//                        (StringUtils.equals(sysMenu.getMenuTag(), (String) permission) ||
//                                StringUtils.equals(sysMenu.getMenuTag(), "*")))
//                    return true;
//            }
        }
        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

}
