package org.yangyi.project.system.service;

import org.yangyi.project.system.po.SysMenu;

import java.util.List;

public interface ISysMenuService {
    void query();

    void list();

    void add();

    void edit();

    void remove();

    List<SysMenu> selectRoleMenus(Long roleId);
}
