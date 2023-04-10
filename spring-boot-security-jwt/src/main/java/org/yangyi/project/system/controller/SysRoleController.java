package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.system.dto.RoleAddDTO;
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
    @PreAuthorize("hasRole('admin')")
    public ApiResponseVO query() {
        return ApiResponseVO.success("query");
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
    public ApiResponseVO remove() {
        return null;
    }

    @PostMapping("/remove/{userId}")
    public ApiResponseVO userRoles(@PathVariable("userId") Long userId) {
        return ApiResponseVO.success(this.sysRoleService.userRoles(userId));
    }

}
