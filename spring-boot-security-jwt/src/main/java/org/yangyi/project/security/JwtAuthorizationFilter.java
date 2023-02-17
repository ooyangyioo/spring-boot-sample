package org.yangyi.project.security;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token有效性验证拦截器
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            JwtAuthenticationToken jwtAuthenticationToken = getAuthentication(request);
            if (null == jwtAuthenticationToken) {
                filterChain.doFilter(request, response);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
        } catch (AuthenticationException exception) {
            this.getAuthenticationEntryPoint().commence(request, response, exception);
            return;
        }
        filterChain.doFilter(request, response);
    }

    protected JwtAuthenticationToken getAuthentication(HttpServletRequest request) throws AuthenticationException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            return null;
        }
        header = header.trim();
        if (!StringUtils.hasText(header) || !header.startsWith(AUTHENTICATION_SCHEME_BEARER)) {
            return null;
        }
        String token = header.substring(7);
        if (!JWTUtil.verify(token, "123456".getBytes())) {
            throw new BadCredentialsException("凭证无效！");
        }
        /**
         * 验证Token有效期
         */
        try {
            JWTValidator.of(token).validateDate();
        } catch (ValidateException e) {
            throw new CredentialsExpiredException("凭证已过期！");
        }

        JWT jwt = JWTUtil.parseToken(token);
        String userString = (String) jwt.getPayload("user");
        JwtUserDetails jwtUserDetails = JSON.parseObject(userString, JwtUserDetails.class);
        return new JwtAuthenticationToken(jwtUserDetails.getUsername(), jwtUserDetails.getPassword(), jwtUserDetails.getAuthorities());
    }

}
