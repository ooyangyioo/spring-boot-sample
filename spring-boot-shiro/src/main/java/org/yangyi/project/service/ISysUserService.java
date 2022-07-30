package org.yangyi.project.service;

import org.yangyi.project.domain.SysUser;

public interface ISysUserService {

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     */
    String doSignup(String username, String password);

    SysUser doLogin(String username, String password);

}
