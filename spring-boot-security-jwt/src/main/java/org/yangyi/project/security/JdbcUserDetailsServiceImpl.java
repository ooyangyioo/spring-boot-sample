package org.yangyi.project.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JdbcUserDetailsServiceImpl implements UserDetailsService {

//    private final SysUserDao sysUserDao;
//
//    @Autowired(required = false)
//    public JdbcUserDetailsServiceImpl(SysUserDao sysUserDao) {
//        this.sysUserDao = sysUserDao;
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SysUserRole sysUserRole = sysUserDao.selectUserAndRoleByUsername(username);
//        if (Objects.isNull(sysUserRole))
//            throw new UsernameNotFoundException("用户不存在!");
//
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        List<SysRole> sysRoleList = sysUserRole.getSysRoles();
//        sysRoleList.forEach(sysRole -> authorities.add(new SimpleGrantedAuthority(sysRole.getRoleCode())));
//        return new JwtUserDetails(
//                sysUserRole.getUsername(),
//                sysUserRole.getPassword(),
//                sysUserRole.getEnabled(),
//                sysUserRole.getExpired(),
//                sysUserRole.getLocked(),
//                sysUserRole.getPasswordExpired(),
//                authorities);

        if (!StringUtils.equals(username, "yangyi"))
            throw new UsernameNotFoundException("用户不存在!");

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> sysRoleList = new ArrayList<String>() {{
            add("admin");
            add("guest");
        }};
        sysRoleList.forEach(sysRole -> authorities.add(new SimpleGrantedAuthority(sysRole)));
        return new JwtUserDetails(
                "yangyi",
                "{bcrypt}$2a$10$32GneFNOwiWyShkOfM54auJjckEfOibePVM966A8/.OsPEcngUUc6", // yangyi
                true,
                false,
                false,
                false,
                authorities);
    }

}
