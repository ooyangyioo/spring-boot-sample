package org.yangyi.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.SysMenuDao;
import org.yangyi.project.entity.SysMenu;
import org.yangyi.project.service.IMenuService;

import java.util.List;

@Service
public class MenuServiceImpl implements IMenuService {

    private SysMenuDao sysMenuDao;

    @Autowired
    public void setSysMenuDao(SysMenuDao sysMenuDao) {
        this.sysMenuDao = sysMenuDao;
    }


    @Override
    public List<SysMenu> list() {
        return sysMenuDao.all();
    }

    @Override
    public boolean add(String menuName, String menuUrl, String menuTag) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuName(menuName);
        sysMenu.setMenuUrl(menuUrl);
        sysMenu.setMenuTag(menuTag);

        int result = sysMenuDao.insert(sysMenu);
        return 0 != result;
    }

    @Override
    public boolean delete(String menuId) {
        int result = sysMenuDao.deleteById(Long.valueOf(menuId));
        return 0 != result;
    }

}
