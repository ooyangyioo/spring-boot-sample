package org.yangyi.project.security;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yangyi.project.system.po.SysMenu;
import org.yangyi.project.system.po.SysRoleMenu;
import org.yangyi.project.system.po.SysUserRole;
import org.yangyi.project.system.service.ISysRoleService;
import org.yangyi.project.system.service.ISysUserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Token有效性验证拦截器
 */
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer ";
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final ISysUserService sysUserService;
    private final ISysRoleService sysRoleService;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationEntryPoint authenticationEntryPoint,
                                  ISysUserService sysUserService,
                                  ISysRoleService sysRoleService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }
        header = header.trim();
        if (!StringUtils.hasText(header) || !header.startsWith(AUTHENTICATION_SCHEME_BEARER)) {
            this.authenticationEntryPoint.commence(request, response, new BadCredentialsException("凭证无效"));
            return;
        }
        String token = header.substring(AUTHENTICATION_SCHEME_BEARER.length());
        if (!JWTUtil.verify(token, "123456".getBytes())) {
            this.authenticationEntryPoint.commence(request, response, new BadCredentialsException("凭证无效"));
            return;
        }
        try {
            //  验证Token有效期
            JWTValidator.of(token).validateDate();
        } catch (ValidateException e) {
            this.authenticationEntryPoint.commence(request, response, new CredentialsExpiredException("凭证已过期"));
            return;
        }

        JWT jwt = JWTUtil.parseToken(token);
        String username = jwt.getPayload("username").toString();
        SysUserRole sysUser = sysUserService.userWithRole(username);
        Collection<UrlGrantedAuthority> authorities = new ArrayList<>();
        sysUser.getSysRoles().forEach(sysRole -> {
            SysRoleMenu sysRoleMenu = sysRoleService.roleWithMenu(sysRole.getRoleId());
            List<String> permissions = new ArrayList<>();
            List<SysMenu> sysMenus = sysRoleMenu.getSysMenus();
            sysMenus.forEach(sysMenu -> {
                permissions.add(sysMenu.getPerms());
            });
            authorities.add(new UrlGrantedAuthority(sysRoleMenu.getRoleKey(), permissions));
        });
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(sysUser.getUserName(), sysUser.getPassword(), authorities));
        filterChain.doFilter(request, response);
    }

}
