package org.yangyi.project.service;

import org.yangyi.project.entity.SysMenu;

import java.util.List;

public interface IMenuService {

    List<SysMenu> list();

    boolean add(String menuName, String menuUrl, String menuTag);

    boolean delete(String menuId);

}
