package org.yangyi.project.security;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.SysMenuDao;
import org.yangyi.project.dao.SysUserDao;
import org.yangyi.project.entity.SysMenu;
import org.yangyi.project.entity.SysRole;
import org.yangyi.project.entity.SysUserRole;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private SysUserDao sysUserDao;
    private SysMenuDao sysMenuDao;

    @Autowired
    public void setSysUserDao(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @Autowired
    public void setSysMenuDao(SysMenuDao sysMenuDao) {
        this.sysMenuDao = sysMenuDao;
    }

    /**
     * 需要构造出 org.springframework.security.core.userdetails.User 对象并返回
     *
     * @param username 用户名
     * @return 查询的用户信息
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserRole sysUserRole = sysUserDao.selectUserAndRoleByUsername(username);
        if (Objects.isNull(sysUserRole))
            throw new UsernameNotFoundException("用户不存在!");
        //  定义权限列表
        Collection<UrlGrantedAuthority> authorities = Lists.newArrayList();
        List<SysRole> sysRoleList = sysUserRole.getSysRoles();
        sysRoleList.forEach(sysRole -> {
            List<SysMenu> sysMenus = sysMenuDao.selectByRoleCode(sysRole.getRoleCode());
            authorities.add(new UrlGrantedAuthority(sysRole.getRoleCode(), sysMenus));
        });
        return new User(
                sysUserRole.getUsername(),
                sysUserRole.getPassword(),
                sysUserRole.getEnabled(),
                sysUserRole.getExpired(),
                sysUserRole.getPasswordExpired(),
                sysUserRole.getLocked(),
                authorities);
    }
}
