package org.yangyi.project.shiro;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 该过滤器需要已登录或记住我的用户才能访问
 * UserFilter为Shiro默认提供，重写是因为在“记住我”功能下退出登录会重定向到登录请求上
 */
public class ShiroUserFilter extends UserFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        ResponseUtil.unauthorizedResponse((HttpServletResponse) response, ResponseVO.failed("请登录！"));
        return false;
    }

}
