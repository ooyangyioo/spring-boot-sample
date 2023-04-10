package org.yangyi.project.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UrlGrantedAuthority implements GrantedAuthority {

    private final String role;

    private final List<String> permissions;

    public UrlGrantedAuthority(String role, List<String> permissions) {
        this.role = role;
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}
