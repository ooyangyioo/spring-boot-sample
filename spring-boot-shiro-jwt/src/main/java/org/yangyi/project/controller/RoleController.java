package org.yangyi.project.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.service.RoleService;
import org.yangyi.project.vo.ResponseVO;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    public ResponseEntity roleList() {
        return ResponseEntity.ok(ResponseVO.success(roleService.roleList()));
    }

    @GetMapping("/add")
    @RequiresPermissions("role:add")
    public ResponseEntity roleAdd(@NotBlank(message = "角色代码不能为空")
                                  @RequestParam(required = false) String roleCode) {
        return ResponseEntity.ok(ResponseVO.success(roleService.roleAdd(roleCode)));
    }

    @GetMapping("/delete")
    public ResponseEntity roleDelete() {
        return ResponseEntity.ok(ResponseVO.success(roleService.roleDelete()));
    }

    @GetMapping("/update")
    public ResponseEntity roleUpdate() {
        return ResponseEntity.ok(ResponseVO.success(roleService.roleUpdate()));
    }

}
