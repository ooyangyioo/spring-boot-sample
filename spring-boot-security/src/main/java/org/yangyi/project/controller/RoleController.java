package org.yangyi.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.entity.SysRole;
import org.yangyi.project.service.IRoleService;
import org.yangyi.project.vo.ResponseVO;

import java.util.List;

@RestController
@RequestMapping("/role")
@Validated
public class RoleController {

    private IRoleService roleService;

    @Autowired
    public void setRoleService(IRoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasPermission('/role/list', 'role:list')")
    public ResponseEntity<ResponseVO<List<SysRole>>> list() {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", roleService.list()));
    }

    @GetMapping("/add")
    @ResponseBody
    @PreAuthorize("hasPermission('/role/add', 'role:add')")
    public ResponseEntity<ResponseVO<Boolean>> add(@RequestParam String roleCode, @RequestParam String roleName) {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", roleService.add(roleCode, roleName)));
    }

    @GetMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasPermission('/role/add', 'role:add')")
    public ResponseEntity<ResponseVO<Boolean>> delete(@RequestParam String roleId) {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", roleService.delete(roleId)));
    }

}
