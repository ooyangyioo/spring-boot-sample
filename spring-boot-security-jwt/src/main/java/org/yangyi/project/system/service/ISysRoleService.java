package org.yangyi.project.system.service;

import org.yangyi.project.system.dto.RoleAddDTO;

public interface ISysRoleService {

    void query();

    void list();

    void add(RoleAddDTO roleAddDTO);

    void edit();

    void remove();

}
