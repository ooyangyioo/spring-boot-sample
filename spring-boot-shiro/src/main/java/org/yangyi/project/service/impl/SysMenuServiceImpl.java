package org.yangyi.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.domain.SysMenu;
import org.yangyi.project.domain.SysMenuTree;
import org.yangyi.project.mapper.SysMenuMapper;
import org.yangyi.project.service.ISysMenuService;

import java.util.Date;
import java.util.List;

@Service
public class SysMenuServiceImpl implements ISysMenuService {

    private SysMenuMapper sysMenuMapper;

    @Autowired
    public void setSysMenuMapper(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<SysMenuTree> list() {
        return sysMenuMapper.selectTreeByParentId(0L);
    }

    @Override
    public Boolean add() {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuName("菜单删除");
        sysMenu.setParentId(1564875468954537986L);
        sysMenu.setMenuSort(4);
        sysMenu.setRoutePath("/sys/menu/remove");
//        sysMenu.setComponentPath("/system/job/index");
        sysMenu.setMenuType("B");
        sysMenu.setPermission("sys:menu:remove");
        sysMenu.setRemark("菜单删除");
        sysMenu.setCreateTime(new Date());
        return sysMenuMapper.insert(sysMenu) == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

}
