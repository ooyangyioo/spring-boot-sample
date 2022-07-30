package org.yangyi.project.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.service.PermissionService;
import org.yangyi.project.vo.ResponseVO;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/list")
    @RequiresPermissions("permission:list")
    public ResponseEntity permissionList() {
        return ResponseEntity.ok(ResponseVO.success(permissionService.permissionList()));
    }

    @GetMapping("/add")
    public ResponseEntity permissionAdd(@NotBlank(message = "角色代码不能为空")
                                            @RequestParam(required = false) String roleCode) {
        return ResponseEntity.ok(ResponseVO.success(permissionService.permissionAdd()));
    }

}
