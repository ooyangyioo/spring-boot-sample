package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.system.dto.SignupDTO;
import org.yangyi.project.system.service.SysUserService;
import org.yangyi.project.web.ResponseVO;

@RestController
@RequestMapping("/user")
public class SysUserController {

    private final SysUserService sysUserService;

    @Autowired
    public SysUserController(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/signup")
    public ResponseVO signup(@Validated @RequestBody SignupDTO signupDTO) {
        return ResponseVO.success(sysUserService.userSignup(signupDTO));
    }

}
