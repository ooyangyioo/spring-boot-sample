package org.yangyi.project.entity;

import java.io.Serializable;
import java.util.List;

public class ShiroUserRole extends ShiroUser implements Serializable {

    private List<ShiroRole> sysRoles;

    public List<ShiroRole> getSysRoles() {
        return sysRoles;
    }

    public void setSysRoles(List<ShiroRole> sysRoles) {
        this.sysRoles = sysRoles;
    }

}
