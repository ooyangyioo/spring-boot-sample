package org.yangyi.project.system.service;

import org.yangyi.project.system.dto.UserSignupDTO;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.po.SysUserRole;
import org.yangyi.project.system.vo.SysUserVO;

public interface ISysUserService {

    SysUserVO userSignup(UserSignupDTO userSignupDTO);

    SysUser userByName(String username);

}
