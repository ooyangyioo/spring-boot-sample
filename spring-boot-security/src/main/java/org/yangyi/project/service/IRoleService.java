package org.yangyi.project.service;

import org.yangyi.project.entity.SysRole;

import java.util.List;

public interface IRoleService {

    List<SysRole> list();

    boolean add(String roleCode, String roleName);

    boolean delete(String roleId);
}
