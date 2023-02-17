package org.yangyi.project.security;

import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.yangyi.project.web.ResponseUtil;
import org.yangyi.project.web.ResponseVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理器
 * 用户验证通过，返回JWT Token
 */
@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        String token = jwtToken(jwtUserDetails, "123456");
        ResponseUtil.okResponse(response, new ResponseVO("1", "成功", token));
    }

    private String jwtToken(JwtUserDetails jwtUserDetails, String jwtKey) {
        DateTime issueDateTime = DateTime.now();
        Map<String, Object> payload = new HashMap<>();
        payload.put(JWTPayload.ISSUED_AT, issueDateTime); //  签发时间
        //  payload.put(JWTPayload.EXPIRES_AT, issueDateTime.offsetNew(DateField.MINUTE, expireMinute));    //  过期时间
        payload.put(JWTPayload.NOT_BEFORE, issueDateTime);    //  生效时间
        payload.put("user", JSON.toJSONString(jwtUserDetails));
        String token = JWTUtil.createToken(payload, jwtKey.getBytes());
        return token;
    }

}
