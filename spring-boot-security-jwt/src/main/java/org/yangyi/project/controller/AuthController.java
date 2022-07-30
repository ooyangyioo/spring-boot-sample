package org.yangyi.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.dao.SysUserDao;
import org.yangyi.project.entity.SysUser;

@RestController
@RequestMapping("/user")
public class AuthController {

    private final SysUserDao sysUserDao;

    @Autowired(required = false)
    public AuthController(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @PostMapping("/info")
    ResponseEntity userInfo(@RequestParam("userId") Long userId) {
        SysUser sysUser = sysUserDao.selectById(userId);
        return ResponseEntity.ok(sysUser);
    }

    @GetMapping("/test")
    ResponseEntity test() {
        return ResponseEntity.ok("Hello World");
    }

}
