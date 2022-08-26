package org.yangyi.project.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.domain.SysUser;
import org.yangyi.project.service.ISysUserService;
import org.yangyi.project.vo.ResponseVO;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/sys/user")
@Validated
public class SysUserController {

    private ISysUserService sysUserService;

    @Autowired
    public void setSysUserService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping(value = "/signup")
    public ResponseEntity<String> doSignup(@NotBlank(message = "用户名不能为空") @RequestParam(required = false) String username,
                                           @NotBlank(message = "密码不能为空") @RequestParam(required = false) String password) {
        return ResponseEntity.ok(sysUserService.doSignup(username, password));
    }

    @GetMapping(value = {"/info"})
    @RequiresPermissions(value = {"sys:user:info"})
    public ResponseEntity<ResponseVO<SysUser>> doInfo() {
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        return ResponseEntity.ok(ResponseVO.success(sysUser));
    }

//    @RequestMapping("/deletePermission")
//    public ResponseEntity deletePermission() {
//        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
//        ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();
//        //  清除当前用户的授权缓存
//        shiroRealm.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
//        //  清除当前用户的认证缓存
//        shiroRealm.clearCachedAuthenticationInfo(SecurityUtils.getSubject().getPrincipals());
//        return ResponseEntity.ok(ResponseVO.success("成功"));
//    }

}
