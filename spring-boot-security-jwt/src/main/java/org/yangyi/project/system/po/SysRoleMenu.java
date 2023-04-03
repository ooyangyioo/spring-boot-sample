package org.yangyi.project.system.po;

import java.io.Serializable;
import java.util.List;

public class SysRoleMenu extends SysRole implements Serializable {

    List<SysMenu> sysMenus;

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<SysMenu> sysMenus) {
        this.sysMenus = sysMenus;
    }
}
