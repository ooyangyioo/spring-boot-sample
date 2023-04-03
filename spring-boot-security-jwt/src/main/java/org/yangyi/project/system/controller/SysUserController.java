package org.yangyi.project.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('admin')")
    public ApiResponseVO info(@RequestParam(required = false) String userId) {
        return ApiResponseVO.success(userId);
    }

    /**
     * 删除用户，使用自定义校验方法
     */
    @PostMapping("/delete")
    @PreAuthorize("@ex.hasRole('admin1')")
    public ApiResponseVO delete(@RequestParam String userId) {
        return ApiResponseVO.success(userId);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('admin')")
    public ApiResponseVO update(@RequestParam String userId) {
        return ApiResponseVO.success(userId);
    }

    @PostMapping("/resetPwd")
//    @PreAuthorize("hasAuthority('admin')")
    @PreAuthorize("hasAnyRole('admin')")
    public ApiResponseVO resetPassword(@RequestParam String userId) {
        return ApiResponseVO.success(userId);
    }

}
