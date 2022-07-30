package org.yangyi.project.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yangyi.project.entity.ShiroUser;
import org.yangyi.project.service.UserService;
import org.yangyi.project.vo.ResponseVO;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户名密码登录
     */
    @GetMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public ResponseEntity<ResponseVO> login(@NotBlank(message = "用户名不能为空") @RequestParam(required = false) String username,
                                            @NotBlank(message = "密码不能为空") @RequestParam(required = false) String password) {
        Subject subject = SecurityUtils.getSubject();
        try {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            subject.login(usernamePasswordToken);
            ShiroUser user = (ShiroUser) subject.getPrincipal();

            DateTime issueDateTime = DateTime.now();
            Map<String, Object> payload = new HashMap<>();
            payload.put(JWTPayload.ISSUED_AT, issueDateTime); //  签发时间
            //  payload.put(JWTPayload.EXPIRES_AT, issueDateTime.offsetNew(DateField.MINUTE, expireMinute));    //  过期时间
            payload.put(JWTPayload.NOT_BEFORE, issueDateTime);    //  生效时间
            payload.put("username", user.getUsername());
            String jwtToken = JWTUtil.createToken(payload, "123456".getBytes());

            return ResponseEntity.ok().body(ResponseVO.success(jwtToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseVO.failed(e.getMessage()));
        }

    }

    @GetMapping(value = "/register")
    @Validated
    public ResponseEntity<ResponseVO> register(@NotBlank(message = "用户名不能为空") @RequestParam(required = false) String username,
                                               @NotBlank(message = "密码不能为空") @RequestParam(required = false) String password) {
        Long userId = userService.userRegister(username, password);
        return ResponseEntity.ok(ResponseVO.success(userId));
    }

    @GetMapping(value = "/info")
    //  @RequiresRoles("admin")
    public ResponseEntity<ResponseVO> info() {

        Subject subject = SecurityUtils.getSubject();
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        System.err.println(realmName);

        return ResponseEntity.ok(new ResponseVO("1", "成功", "info"));
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.getPrincipals() != null) {
//            SysUser sysUser = (SysUser) subject.getPrincipals().getPrimaryPrincipal();
//        }
//        SecurityUtils.getSubject().logout();
        return ResponseEntity.ok().build();
    }

}
