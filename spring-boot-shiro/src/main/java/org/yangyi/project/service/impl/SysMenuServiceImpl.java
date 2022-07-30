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
        sysMenu.setMenuName("任务删除");
        sysMenu.setMenuSort(4);
        sysMenu.setParentId(1540217126953029634L);
        sysMenu.setRoutePath("/system/job/delete");
//        sysMenu.setComponentPath("/system/job/index");
        sysMenu.setMenuType("B");
        sysMenu.setPermission("system:job:delete");
        sysMenu.setRemark("任务删除");
        sysMenu.setCreateTime(new Date());
        return sysMenuMapper.insert(sysMenu) == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

}
