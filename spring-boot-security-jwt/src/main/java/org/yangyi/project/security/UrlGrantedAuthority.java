package org.yangyi.project.security;

import org.springframework.security.core.GrantedAuthority;
import org.yangyi.project.system.po.SysMenu;

import java.util.List;

public class UrlGrantedAuthority implements GrantedAuthority {

    private final String role;
    private final List<SysMenu> sysMenus;

    public UrlGrantedAuthority(String role, List<SysMenu> sysMenus) {
        this.role = role;
        this.sysMenus = sysMenus;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

}
