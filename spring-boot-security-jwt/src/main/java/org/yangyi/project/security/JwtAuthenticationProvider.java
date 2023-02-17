package org.yangyi.project.security;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JdbcUserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationProvider(JdbcUserDetailsServiceImpl userDetailsService,
                                     PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = (String) authentication.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword()))
            throw new BadCredentialsException("密码错误");
        if (!userDetails.isEnabled())
            throw new DisabledException("账号不可用");
        if (userDetails.isAccountNonExpired())
            throw new AccountExpiredException("账号已过期");
        if (userDetails.isAccountNonLocked())
            throw new LockedException("账号已锁定");
        if (userDetails.isCredentialsNonExpired())
            throw new CredentialsExpiredException("密码已过期");

        return new JwtAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
    }

    /**
     * 是否支持
     * 决定是否会进入authenticate 方法
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

}
