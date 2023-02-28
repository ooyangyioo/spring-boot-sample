package org.yangyi.project.system.service;

import org.yangyi.project.system.dto.UserSignupDTO;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.po.SysUserRole;

public interface ISysUserService {

    SysUser userSignup(UserSignupDTO userSignupDTO);

    SysUser userByName(String username);

    SysUserRole userWithRole(String username);
}
