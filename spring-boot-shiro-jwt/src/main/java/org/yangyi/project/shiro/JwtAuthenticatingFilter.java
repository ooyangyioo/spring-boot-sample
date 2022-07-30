package org.yangyi.project.shiro;


import cn.hutool.jwt.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtAuthenticatingFilter extends AuthenticatingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticatingFilter.class);

    private static final int tokenRefreshInterval = 300;

    public JwtAuthenticatingFilter() {
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        //  对于OPTION请求做拦截，不做token校验
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name()))
            return false;
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        LOGGER.info("JwtAuthenticatingFilter--->isAccessAllowed");
        /**
         * 判断是否是登录请求，如果是直接放行
         * ShiroConfig 中如果不添加 chainDefinition.addPathDefinition("/user/login", "anon");
         * 将会进入到该过滤器中，此处再次判断
         */
        if (this.isLoginRequest(request, response))
            return true;

        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) {
            LOGGER.error("Not found any token");
        } catch (Exception e) {
            LOGGER.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        LOGGER.info("JwtAuthenticatingFilter--->createToken");
        String jwtToken = getAuthorizationHeader(servletRequest);
        if (StringUtils.isBlank(jwtToken))
            return null;
        if (!JWTUtil.verify(jwtToken, "123456".getBytes()))
            throw null;

        return new JwtAuthenticationToken(jwtToken);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        LOGGER.info("JwtAuthenticatingFilter--->onAccessDenied");
        ResponseUtil.unauthorizedResponse((HttpServletResponse) servletResponse, new ResponseVO("0", "Token错误！"));
        return false;
    }

    protected String getAuthorizationHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return StringUtils.removeStart(header, "Bearer ");
    }

    protected boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }

}
