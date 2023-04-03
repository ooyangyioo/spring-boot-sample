package org.yangyi.project.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yangyi.project.system.po.SysRole;
import org.yangyi.project.system.po.SysUserRole;
import org.yangyi.project.system.service.ISysUserService;

import java.util.*;

@Service
public class JdbcUserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUserDetailsServiceImpl.class);
    private final ISysUserService sysUserService;

    @Autowired
    public JdbcUserDetailsServiceImpl(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserRole sysUserRole = null;
        try {
            sysUserRole = sysUserService.userWithRole(username);
        } catch (Exception e) {
            logger.error("从数据库或缓存未查询到用户：[{}]；错误信息：{}", username, e.getMessage());
        }
        if (Objects.isNull(sysUserRole))
            throw new UsernameNotFoundException("用户不存在");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> sysRoleList = sysUserRole.getSysRoles();
        sysRoleList.forEach(sysRole -> authorities.add(new SimpleGrantedAuthority(sysRole.getRoleKey())));
        return new JwtUserDetails(sysUserRole.getUserName(), sysUserRole.getPassword(), authorities);
    }

}
