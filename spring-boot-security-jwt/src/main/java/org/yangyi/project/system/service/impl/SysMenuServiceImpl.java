package org.yangyi.project.system.service.impl;

import org.springframework.stereotype.Service;
import org.yangyi.project.system.dao.SysMenuMapper;
import org.yangyi.project.system.po.SysMenu;
import org.yangyi.project.system.service.ISysMenuService;

import java.util.List;

@Service
public class SysMenuServiceImpl implements ISysMenuService {

    private final SysMenuMapper sysMenuMapper;

    public SysMenuServiceImpl(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public void query() {

    }

    @Override
    public void list() {

    }

    @Override
    public void add() {

    }

    @Override
    public void edit() {

    }

    @Override
    public void remove() {

    }

    @Override
    public List<SysMenu> selectRoleMenus(Long roleId) {
        return sysMenuMapper.selectRoleMenus(roleId);
    }
}
