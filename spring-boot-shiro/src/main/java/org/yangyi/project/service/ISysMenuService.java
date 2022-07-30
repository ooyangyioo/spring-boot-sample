package org.yangyi.project.service;

import org.yangyi.project.domain.SysMenuTree;

import java.util.List;

public interface ISysMenuService {

    List<SysMenuTree> list();

    Boolean add();
}
