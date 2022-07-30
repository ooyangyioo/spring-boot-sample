package org.yangyi.project.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.service.ISysRoleService;
import org.yangyi.project.vo.ResponseVO;

@RestController
@RequestMapping("/system/role")
@Validated
public class SysRoleController {

    private ISysRoleService sysRoleService;

    @Autowired
    public void setSysRoleService(ISysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    @GetMapping(value = {"/list"})
    @RequiresPermissions(value = {"system:menu:list"})
    public ResponseEntity list() {
        return ResponseEntity.ok(ResponseVO.success(sysRoleService.list()));
    }

    @GetMapping("/add")
    public ResponseEntity add() {
        return ResponseEntity.ok(ResponseVO.success(sysRoleService.add()));
    }

}
