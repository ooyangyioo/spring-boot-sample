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
import org.yangyi.project.system.po.SysRole;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.service.ISysMenuService;
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
    private final ISysMenuService sysMenuService;

    @Autowired
    public JwtAuthorizationFilter(AuthenticationEntryPoint authenticationEntryPoint,
                                  ISysUserService sysUserService,
                                  ISysRoleService sysRoleService,
                                  ISysMenuService sysMenuService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
        this.sysMenuService = sysMenuService;
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
        SysUser sysUser = sysUserService.userByName(username);
        List<SysRole> sysRoles = sysRoleService.userRoles(sysUser.getUserId());
        Collection<UrlGrantedAuthority> authorities = new ArrayList<>();
        List<String> perms = new ArrayList<>();
        sysRoles.forEach(sysRole -> {
            Long roleId = sysRole.getRoleId();
            List<SysMenu> sysMenus = this.sysMenuService.selectRoleMenus(roleId);
            sysMenus.forEach(sysMenu -> perms.add(sysMenu.getPerms()));
            authorities.add(new UrlGrantedAuthority(sysRole.getRoleKey(), perms));
        });
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(sysUser, sysUser.getPassword(), authorities));
        filterChain.doFilter(request, response);
    }

}
