package org.yangyi.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.entity.SysUser;
import org.yangyi.project.service.IUserService;
import org.yangyi.project.vo.ResponseVO;

import java.util.Objects;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    @ResponseBody
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
//    @PreAuthorize("hasPermission('/user/info', 'user:info')")
    @PreAuthorize("hasPermission(#userId,'targetType', 'r')")
//    @PreAuthorize("hasRole('USER') and hasPermission('/user/info', 'user:info')")
    public ResponseEntity<ResponseVO<SysUser>> info(@RequestParam(value = "userId", required = false) String userId) {
        SysUser sysUser;
        if (!Objects.isNull(userId))
            sysUser = userService.selectUser(Long.valueOf(userId));
        else {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            sysUser = userService.selectUser(user.getUsername());
        }
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", sysUser));
    }

    @GetMapping("/signup")
    @ResponseBody
    public ResponseEntity<ResponseVO<Long>> signup(@RequestParam("username") String username,
                                                   @RequestParam("password") String password,
                                                   @RequestParam("phone") String phone) {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", userService.signup(username, password, phone)));
    }

    @GetMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseVO<Boolean>> userDelete(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(new ResponseVO<>("1", "成功", userService.userDelete(userId)));
    }

}
