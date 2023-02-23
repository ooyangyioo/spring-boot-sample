package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.system.dto.RoleAddDTO;
import org.yangyi.project.system.service.ISysRoleService;
import org.yangyi.project.web.ResponseVO;

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
    public ResponseVO query() {
        return ResponseVO.success("query");
    }

    @PostMapping("/list")
    public ResponseVO list() {
        return null;
    }

    @PostMapping("/add")
    public ResponseVO add(@RequestBody @Validated RoleAddDTO roleAddDTO) {
        this.sysRoleService.add(roleAddDTO);
        return null;
    }

    @PostMapping("/edit")
    public ResponseVO edit() {
        return null;
    }

    @PostMapping("/remove")
    public ResponseVO remove() {
        return null;
    }

}
