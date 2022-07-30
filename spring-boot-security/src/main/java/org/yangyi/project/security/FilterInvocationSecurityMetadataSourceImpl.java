package org.yangyi.project.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.yangyi.project.dao.SysRoleDao;

import java.util.Collection;
import java.util.List;

@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterInvocationSecurityMetadataSourceImpl.class);

    private SysRoleDao sysRoleDao;

    @Autowired
    public void setSysRoleDao(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<String> sysRoles = sysRoleDao.selectByMenuUrl(requestUrl);
        if (sysRoles.isEmpty())
            return null;
        String[] sysRoleArr = new String[sysRoles.size()];
        LOGGER.info("访问路径：{} 需要权限：{}", requestUrl, sysRoles.toArray(sysRoleArr));
        return SecurityConfig.createList(sysRoles.toArray(sysRoleArr));
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
