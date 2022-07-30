package org.yangyi.project.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yangyi.project.domain.SysMenu;
import org.yangyi.project.domain.SysRole;
import org.yangyi.project.domain.SysUser;
import org.yangyi.project.mapper.SysMenuMapper;
import org.yangyi.project.mapper.SysRoleMapper;
import org.yangyi.project.mapper.SysUserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShiroRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroRealm.class);

    private SysUserMapper sysUserMapper;
    private SysRoleMapper sysRoleMapper;
    private SysMenuMapper sysMenuMapper;

    @Autowired
    public void setSysUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Autowired
    public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Autowired
    public void setSysMenuMapper(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    /**
     * 授权
     * 用户访问某个接口时需要权限认证时会调用改方法
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        LOGGER.info("用户：{} 的授权认证", user.getUserName());
        List<String> roles = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        List<SysRole> sysRoles = sysRoleMapper.selectUserRoles(user.getUserId());
        sysRoles.forEach(sysRole -> {
            roles.add(sysRole.getRoleKey());
            List<SysMenu> sysMenuList = sysMenuMapper.selectByRoleId(sysRole.getRoleId());
            sysMenuList.forEach(sysMenu -> {
                if (StringUtils.isNotBlank(sysMenu.getPermission())) {
                    permissions.add(sysMenu.getPermission());
                }
            });
        });
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRoles(roles);
        authorizationInfo.addStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 认证
     * 登录时会执行该方法
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        LOGGER.info("用户：{} 的登录认证", username);
        SysUser user = sysUserMapper.selectByUsername(username);
        if (Objects.isNull(user))
            throw new UnknownAccountException("用户不存在！");
        if (1 == user.getAccountLocked())
            throw new LockedAccountException("用户被锁定！");
        if (1 == user.getAccountEnabled())
            throw new DisabledAccountException("账户被停用！");
        if (1 == user.getPasswordExpired())
            throw new ExpiredCredentialsException("密码已过期！");
        return new SimpleAuthenticationInfo(user, user.getPasswd(), new ShiroByteSource(user.getSalt()), getName());
    }

    /**
     * 清除当前用户的授权缓存
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.debug("清除用户的授权缓存");
        super.clearCachedAuthorizationInfo(principals);
    }

    /**
     * 清除当前用户的认证缓存
     */
    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        LOGGER.debug("清除用户的认证缓存");
        super.clearCachedAuthenticationInfo(principals);
    }

    /**
     * 清除当前用户的认证缓存和授权缓存
     */
    @Override
    public void clearCache(PrincipalCollection principals) {
        LOGGER.debug("退出登录，清除缓存");
        super.clearCache(principals);
    }

    /**
     * 自定义方法
     * 清除所有用户的授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法
     * 清除所有用户的认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法
     * 清除所有用户的认证缓存和授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthorizationInfo();
        clearAllCachedAuthenticationInfo();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return super.supports(token);
    }

}
