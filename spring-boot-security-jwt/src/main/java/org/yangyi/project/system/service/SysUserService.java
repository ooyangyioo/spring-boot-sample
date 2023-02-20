package org.yangyi.project.system.service;

import org.yangyi.project.system.dto.SignupDTO;
import org.yangyi.project.system.po.SysUser;

public interface SysUserService {

    SysUser userSignup(SignupDTO signupDTO);

}
