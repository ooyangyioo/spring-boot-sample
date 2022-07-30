package org.yangyi.project.service;

import org.yangyi.project.entity.SysUser;

public interface IUserService {

    SysUser selectUser(Long id);

    SysUser selectUser(String username);

    Long signup(String username, String password, String phone);

    boolean userDelete(String userId);
}
