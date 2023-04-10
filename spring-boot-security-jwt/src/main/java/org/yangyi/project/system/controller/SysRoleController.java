package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.system.dto.RoleAddDTO;
import org.yangyi.project.system.po.SysUser;
import org.yangyi.project.system.service.ISysRoleService;
import org.yangyi.project.web.ApiResponseVO;

@RestController
@RequestMapping("/role")
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @Autowired
    public SysRoleController(ISysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @PostMapping("/query")
    public ApiResponseVO query(@RequestParam(required = false) Long userId) {
        if (null == userId)
            userId = ((SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return ApiResponseVO.success(this.sysRoleService.userRoles(userId));
    }

    @PostMapping("/list")
    @PreAuthorize("hasPermission(null , 'system:role:list')")
    public ApiResponseVO list() {
        return null;
    }

    @PostMapping("/add")
    public ApiResponseVO add(@RequestBody @Validated RoleAddDTO roleAddDTO) {
        this.sysRoleService.add(roleAddDTO);
        return null;
    }

    @PostMapping("/edit")
    public ApiResponseVO edit() {
        return null;
    }

    @PostMapping("/remove")
    @PreAuthorize("hasRole('admin') or hasPermission(null , 'system:role:list')")
    public ApiResponseVO remove() {
        return null;
    }

}
