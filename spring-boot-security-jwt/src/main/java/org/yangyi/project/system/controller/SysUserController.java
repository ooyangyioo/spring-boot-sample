package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.system.service.SysUserService;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    public SysUserController(SysUserService sysUserService) {

    }



}
