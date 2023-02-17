package org.yangyi.project.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

@Component
public class JwtFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        System.err.println(filterInvocation.getRequestUrl());
        System.err.println(filterInvocation.getRequest().getMethod());

        AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/**", HttpMethod.POST.name());
        System.err.println(antPathRequestMatcher.matches(filterInvocation.getHttpRequest()));
        ConfigAttribute configAttribute = new ConfigAttribute() {
            @Override
            public String getAttribute() {
                return null;
            }
        };

        return SecurityConfig.createList("ROLE_ANONYMOUS");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        System.err.println("22");
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        System.err.println("33" + clazz);
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
