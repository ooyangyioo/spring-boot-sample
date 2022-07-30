package org.yangyi.project.entity.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 登录用户信息
     */
    private final Object principal;

    /**
     * 密码
     */
    private Object credentials;

    /**
     * 创建一个未认证的授权令牌
     * 传入的principal是用户名
     *
     * @param principal
     * @param credentials
     */
    public JwtAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    /**
     * 创建一个已认证的授权令牌
     * 这个方法应该由AuthenticationProvider来调用
     * 传入的principal为UserDetails
     *
     * @param principal
     * @param credentials
     * @param authorities
     */
    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

}
