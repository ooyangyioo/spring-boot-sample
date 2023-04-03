package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.system.dto.UserSignupDTO;
import org.yangyi.project.system.service.ISysUserService;
import org.yangyi.project.web.ApiResponseVO;

@RestController
@RequestMapping("/user")
public class SysUserController {

    private final ISysUserService sysUserService;

    @Autowired
    public SysUserController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/signup")
    public ApiResponseVO signup(@Validated @RequestBody UserSignupDTO userSignupDTO) {
        return ApiResponseVO.success(sysUserService.userSignup(userSignupDTO));
    }

    @PostMapping("/info")
    public ApiResponseVO info(@RequestParam(required = false) String userId) {
        System.err.println(userId);
        return ApiResponseVO.success(userId);
    }

}
