package org.yangyi.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yangyi.project.system.dao.SysUserMapper;
import org.yangyi.project.system.po.SysUser;

import java.util.*;

@Service
public class JdbcUserDetailsServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Autowired
    public JdbcUserDetailsServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectByUserName(username);
        if (Objects.isNull(sysUser))
            throw new UsernameNotFoundException("用户不存在");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> sysRoleList = new ArrayList<>() {{
            add("admin");
            add("guest");
        }};
        sysRoleList.forEach(sysRole -> authorities.add(new SimpleGrantedAuthority(sysRole)));
        return new JwtUserDetails(
                sysUser.getUserName(),
                sysUser.getPassword(),
                true,
                false,
                false,
                false,
                authorities);
    }

}
