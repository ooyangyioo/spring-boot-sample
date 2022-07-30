package org.yangyi.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.SysUserDao;
import org.yangyi.project.entity.SysRole;
import org.yangyi.project.entity.SysUser;
import org.yangyi.project.entity.SysUserRole;
import org.yangyi.project.entity.security.JwtUserDetails;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserDao sysUserDao;

    @Autowired(required = false)
    public UserDetailsServiceImpl(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserRole sysUserRole = sysUserDao.selectUserAndRoleByUsername(username);
        if (Objects.isNull(sysUserRole))
            throw new UsernameNotFoundException("用户不存在!");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<SysRole> sysRoleList = sysUserRole.getSysRoles();
        sysRoleList.forEach(sysRole -> authorities.add(new SimpleGrantedAuthority(sysRole.getRoleCode())));
        return new JwtUserDetails(
                sysUserRole.getUsername(),
                sysUserRole.getPassword(),
                sysUserRole.getEnabled(),
                sysUserRole.getExpired(),
                sysUserRole.getLocked(),
                sysUserRole.getPasswordExpired(),
                authorities);
    }

}
