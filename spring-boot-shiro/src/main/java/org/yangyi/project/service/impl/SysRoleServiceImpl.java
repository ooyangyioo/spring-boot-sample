package org.yangyi.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yangyi.project.domain.SysRole;
import org.yangyi.project.mapper.SysRoleMapper;
import org.yangyi.project.service.ISysRoleService;

import java.util.Date;
import java.util.List;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    private SysRoleMapper sysRoleMapper;

    @Autowired
    public void setSysRoleMapper(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public List<SysRole> list() {
        return sysRoleMapper.list();
    }

    @Override
    public Boolean add() {
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("用户");
        sysRole.setRoleKey("USER");
        sysRole.setCreateTime(new Date());
        int result = sysRoleMapper.insert(sysRole);
        return result == 0 ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public List<SysRole> userRoles() {
        return null;
    }

}
