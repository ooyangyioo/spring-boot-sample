package org.yangyi.project.security;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.yangyi.project.web.ResponseUtil;
import org.yangyi.project.web.ResponseVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * 登录成功处理器
 * 用户验证通过，返回JWT Token
 */
@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        Date signDate=new Date();
        String token = JWTUtil.createToken(new HashMap<>() {{
            //签发时间
            put(JWTPayload.ISSUED_AT, signDate);
            //生效时间
            put(JWTPayload.NOT_BEFORE, signDate);
            //过期时间 最大有效期1天强制过期
            put(JWTPayload.EXPIRES_AT, DateUtil.offsetDay(signDate,1));
            put("username", jwtUserDetails.getUsername());
        }}, JWTSignerUtil.hs256("123456".getBytes()));
        ResponseUtil.okResponse(response, ResponseVO.success(token));
    }

}
