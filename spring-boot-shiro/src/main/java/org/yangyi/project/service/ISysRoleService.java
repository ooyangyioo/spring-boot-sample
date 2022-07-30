package org.yangyi.project.service;

import org.yangyi.project.domain.SysRole;

import java.util.List;

public interface ISysRoleService {

    List<SysRole> list();

    Boolean add();

    List<SysRole> userRoles();

}
