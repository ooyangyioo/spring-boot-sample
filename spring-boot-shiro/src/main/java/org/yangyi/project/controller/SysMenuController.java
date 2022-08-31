package org.yangyi.project.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.annotation.TestAnnotation;
import org.yangyi.project.service.ISysMenuService;
import org.yangyi.project.vo.ResponseVO;

import java.util.Map;

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
    @TestAnnotation(clazz = Map.class)
    public ResponseEntity list(Map<String, String> data) {
        return ResponseEntity.ok(ResponseVO.success(sysMenuService.list()));
    }

    @GetMapping(value = {"/add"})
    @RequiresRoles(value = {"Admin"})
    public ResponseEntity add() {
        return ResponseEntity.ok(ResponseVO.success(sysMenuService.add()));
    }

}
