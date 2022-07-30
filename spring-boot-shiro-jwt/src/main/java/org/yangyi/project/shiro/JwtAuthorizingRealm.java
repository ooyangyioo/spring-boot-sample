package org.yangyi.project.shiro;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.yangyi.project.entity.ShiroUser;
import org.yangyi.project.service.UserService;


/**
 * 自定义Jwt Token认证
 * 基于HMAC（ 散列消息认证码）的控制域
 */
public class JwtAuthorizingRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthorizingRealm.class);

    private static final String AUTHORIZING_REALM_NAME = "JwtAuthorizingRealm";

    @Autowired
    private UserService userService;

    public JwtAuthorizingRealm() {
        setName(AUTHORIZING_REALM_NAME);
        this.setCredentialsMatcher(new JwtCredentialsMatcher());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtAuthenticationToken;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOGGER.info("JwtAuthorizingRealm--->doGetAuthenticationInfo");
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticationToken;
        String token = jwtAuthenticationToken.getToken();
        JWT jwt = JWTUtil.parseToken(token);
        String username = (String) jwt.getPayload("username");

        ShiroUser sysUser = userService.getUserInfo(username);
//        if(user == null)
//            throw new AuthenticationException("token过期，请重新登录");
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getSalt(), "jwtRealm");
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), getName());

        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.info("JwtAuthorizingRealm--->doGetAuthorizationInfo");
        return new SimpleAuthorizationInfo();
    }

}
