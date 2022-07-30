package org.yangyi.project.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yangyi.project.util.ResponseUtil;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AnyRolesAuthorizationFilter extends AuthorizationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnyRolesAuthorizationFilter.class);

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        LOGGER.info("AnyRolesAuthorizationFilter--->postHandle");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        LOGGER.info("AnyRolesAuthorizationFilter--->isAccessAllowed");
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) { //没有角色限制，有权限访问
            return true;
        }
        for (String role : rolesArray) {
            if (subject.hasRole(role)) //若当前用户是rolesArray中的任何一个，则有权限访问
                return true;
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        LOGGER.info("AnyRolesAuthorizationFilter--->onAccessDenied");
        ResponseUtil.unauthorizedResponse((HttpServletResponse) response, new ResponseVO("0", "无权限访问"));
        return false;
    }

}
