package org.yangyi.project.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;

public class JwtAuthenticationToken implements HostAuthenticationToken {

	private String token;
    private String host;

    public JwtAuthenticationToken(String token) {
        this(token, null);
    }

    public JwtAuthenticationToken(String token, String host) {
        this.token = token;
        this.host = host;
    }

    public String getToken(){
        return this.token;
    }

    public String getHost() {
        return host;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString(){
        return token + ':' + host;
    }
}
