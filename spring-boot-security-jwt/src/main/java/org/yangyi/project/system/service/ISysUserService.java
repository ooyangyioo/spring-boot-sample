package org.yangyi.project.system.service;

import org.yangyi.project.system.dto.SignupDTO;
import org.yangyi.project.system.po.SysUser;

public interface ISysUserService {

    SysUser userSignup(SignupDTO signupDTO);

    SysUser userByName(String username);
}
