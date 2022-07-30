package org.yangyi.project.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义验证器
 * 需要配合Controller 中的登录接口使用，在该Filter做一些其他操作
 * 与ShiroAuthenticationFilter 的功能是两种方式
 */
public class ShiroAuthenticationFilterWithController extends FormAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroAuthenticationFilterWithController.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        LOGGER.info("AccessAllowed");
        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        LOGGER.info("onAccessDenied");
        return super.onAccessDenied(request, response);
    }

}
