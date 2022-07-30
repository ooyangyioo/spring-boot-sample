package org.yangyi.project.shiro;

import com.mysql.cj.exceptions.PasswordExpiredException;
import org.apache.shiro.authc.*;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义验证器
 */
public class ShiroAuthenticationFilter extends FormAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);

    /**
     * 返回 false 将会拦截并执行 onAccessDenied 方法
     * 返回 true  将直接执行请求
     * 登录请求 直接返回 false 到 onAccessDenied 方法中判断
     * 不是登录请求判断是否登录
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginRequest(request, response))
            return false;
        if (isPermissive(mappedValue))
            return true;

        Subject subject = getSubject(request, response);
        return subject.isAuthenticated() && subject.getPrincipal() != null;
    }

    /**
     * 如果是登陆请求，执行登录操作
     * 不是登录请求，返回 401.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (!StringUtils.hasText(getUsername(request))) {
                ResponseUtil.okResponse((HttpServletResponse) response, ResponseVO.failed("请输入用户名！"));
                return false;
            }
            if (!StringUtils.hasText(getPassword(request))) {
                ResponseUtil.okResponse((HttpServletResponse) response, ResponseVO.failed("请输入密码！"));
                return false;
            }
            return executeLogin(request, response);
        } else {
            ResponseUtil.unauthorizedResponse((HttpServletResponse) response, ResponseVO.failed("请登录！"));
            return false;
        }
    }

    /**
     * 登录成功
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        ResponseUtil.okResponse((HttpServletResponse) response, ResponseVO.failed("登录成功！"));
        return false;
    }

    /**
     * 登陆失败
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        LOGGER.warn("用户登录失败：{}", e.getMessage());
        if (e instanceof LockedAccountException) {
            ResponseUtil.serverResponse((HttpServletResponse) response, ResponseVO.failed("用户被锁定！"));
        } else if (e instanceof UnknownAccountException) {
            ResponseUtil.serverResponse((HttpServletResponse) response, ResponseVO.failed("用户不存在！"));
        } else if (e instanceof ExpiredCredentialsException) {
            ResponseUtil.serverResponse((HttpServletResponse) response, ResponseVO.failed("密码已过期！"));
        } else if (e instanceof DisabledAccountException) {
            ResponseUtil.serverResponse((HttpServletResponse) response, ResponseVO.failed("账户不可用！"));
        } else if (e instanceof IncorrectCredentialsException) {
            ResponseUtil.serverResponse((HttpServletResponse) response, ResponseVO.failed("密码错误！"));
        } else {
            ResponseUtil.serverResponse((HttpServletResponse) response, ResponseVO.failed("其他错误，请完善！"));
        }
        return false;
    }

}
