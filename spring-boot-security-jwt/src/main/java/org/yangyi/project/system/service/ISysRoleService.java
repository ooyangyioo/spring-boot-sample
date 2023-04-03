package org.yangyi.project.system.service;

import org.yangyi.project.system.dto.RoleAddDTO;
import org.yangyi.project.system.po.SysRoleMenu;

public interface ISysRoleService {

    void query();

    void list();

    void add(RoleAddDTO roleAddDTO);

    void edit();

    void remove();

    SysRoleMenu roleWithMenu(Long roleId);

}
