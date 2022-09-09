package org.yangyi.project.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.service.ISysMenuService;
import org.yangyi.project.vo.ResponseVO;

@RestController
@RequestMapping("/sys/menu")
@Validated
public class SysMenuController {

    private ISysMenuService sysMenuService;

    @Autowired
    public void setSysMenuService(ISysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @GetMapping(value = {"/list"})
    @RequiresPermissions(value = {"sys:menu:list"})
    public ResponseEntity list() {
        return ResponseEntity.ok(ResponseVO.success(sysMenuService.list()));
    }

    @GetMapping(value = {"/info"})
    public ResponseEntity info() {
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = {"/add"})
    @RequiresRoles(value = {"Admin"})
    public ResponseEntity add() {
        return ResponseEntity.ok(ResponseVO.success(sysMenuService.add()));
    }

    @GetMapping(value = {"/edit"})
    public ResponseEntity edit() {
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = {"/remove"})
    public ResponseEntity remove() {
        return ResponseEntity.ok(null);
    }

}
