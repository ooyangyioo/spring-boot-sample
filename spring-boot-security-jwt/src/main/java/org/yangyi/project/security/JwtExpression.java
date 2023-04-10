package org.yangyi.project.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限校验方法
 */
@Component("ex")
public class JwtExpression {

    public boolean hasRole(String role) {
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UrlGrantedAuthority> urlGrantedAuthorities = (List<UrlGrantedAuthority>) authentication.getAuthorities();
        for (UrlGrantedAuthority urlGrantedAuthority : urlGrantedAuthorities) {
            if (StringUtils.equals(urlGrantedAuthority.getAuthority(), role)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAuthority(String authority) {
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UrlGrantedAuthority> urlGrantedAuthorities = (List<UrlGrantedAuthority>) authentication.getAuthorities();
        for (UrlGrantedAuthority urlGrantedAuthority : urlGrantedAuthorities) {
//            List<String> permissions = urlGrantedAuthority.getPermissions();
//            for (String permission : permissions) {
//                if (StringUtils.equals(permission, authority)) {
//                    return true;
//                }
//            }
        }

        urlGrantedAuthorities.forEach(urlGrantedAuthority -> {
            System.err.println(urlGrantedAuthority.getAuthority());
        });

        return true;
    }

}
