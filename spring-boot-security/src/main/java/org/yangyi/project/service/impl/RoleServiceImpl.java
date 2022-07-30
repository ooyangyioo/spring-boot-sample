package org.yangyi.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.dao.SysRoleDao;
import org.yangyi.project.entity.SysRole;
import org.yangyi.project.service.IRoleService;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    private SysRoleDao sysRoleDao;

    @Autowired
    public void setSysRoleDao(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    @Override
    public List<SysRole> list() {
        return sysRoleDao.selectAll();
    }

    @Override
    public boolean add(String roleCode, String roleName) {
        SysRole sysRole = new SysRole();
        sysRole.setRoleCode(roleCode);
        sysRole.setRoleName(roleName);
        int result = sysRoleDao.insert(sysRole);
        return result != 0;
    }

    @Override
    public boolean delete(String roleId) {
        int result = sysRoleDao.delete(Long.valueOf(roleId));
        return 0 != result;
    }
}
