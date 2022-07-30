package org.yangyi.project.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义登出Filter,Shiro提供有默认登出过滤器LogoutFilter
 */
public class ShiroLogoutFilter extends LogoutFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = this.getSubject(request, response);
        subject.logout();
        ResponseUtil.okResponse((HttpServletResponse) response, ResponseVO.failed("登出成功！"));
        return false;
    }

}
