package org.yangyi.project.system.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.yangyi.project.system.dao.SysRoleMapper;
import org.yangyi.project.system.dto.RoleAddDTO;
import org.yangyi.project.system.po.SysRole;
import org.yangyi.project.system.po.SysRoleMenu;
import org.yangyi.project.system.service.ISysRoleService;

@Service
public class SysRoleServiceImpl implements ISysRoleService {

    private final SysRoleMapper sysRoleMapper;

    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public void query() {

    }

    @Override
    public void list() {

    }

    @Override
    public void add(RoleAddDTO roleAddDTO) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(roleAddDTO, sysRole);
        sysRoleMapper.insert(sysRole);
    }

    @Override
    public void edit() {

    }

    @Override
    public void remove() {

    }

    @Override
    public SysRoleMenu roleWithMenu(Long roleId) {
        return this.sysRoleMapper.selectRoleWithMenu(roleId);
    }
}
