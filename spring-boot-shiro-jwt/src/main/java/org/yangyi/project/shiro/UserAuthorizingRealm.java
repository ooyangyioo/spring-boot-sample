package org.yangyi.project.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yangyi.project.entity.ShiroUser;
import org.yangyi.project.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用于用户名密码登录及权限认证
 */
public class UserAuthorizingRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthorizingRealm.class);

    private static final String AUTHORIZING_REALM_NAME = "UserAuthorizingRealm";

    public static final String ENCRYPT_SALT = "test";

    @Autowired
    private UserService userService;

    public UserAuthorizingRealm() {
        setName(AUTHORIZING_REALM_NAME);

        //  HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(1024);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        setCredentialsMatcher(hashedCredentialsMatcher);
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
     * 用户认证
     * 用户登录执行
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        ShiroUser sysUser = userService.getUserInfo(username);
        if (Objects.isNull(sysUser))
            throw new UnknownAccountException("用户不存在！");
        if (sysUser.getLocked())
            throw new LockedAccountException("用户被锁定！");

        return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), ByteSource.Util.bytes(ENCRYPT_SALT), getName());
    }

    /**
     * 用户鉴权
     * 判断权限时才回回调该方法，登录不会回调
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.info("UserAuthorizingRealm--->doGetAuthorizationInfo");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        ShiroUser user = (ShiroUser) principals.getPrimaryPrincipal();

        List<String> roles = new ArrayList<>();
        roles.add("admin");
        simpleAuthorizationInfo.addRoles(roles);

        List<String> permissions = new ArrayList<>();
        permissions.add("user:info");
        permissions.add("role:add");
        permissions.add("permission:list");
        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }


}
