package org.yangyi.project.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.vo.ResponseVO;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {

    private DefaultKaptcha defaultKaptcha;

    @Autowired
    public void setDefaultKaptcha(DefaultKaptcha defaultKaptcha) {
        this.defaultKaptcha = defaultKaptcha;
    }

    @GetMapping("/create")
    public void createKaptcha(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            servletRequest.getSession().setAttribute("picCode", createText);
            // 使用生成的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            servletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        servletResponse.setHeader("Cache-Control", "no-store");
        servletResponse.setHeader("Pragma", "no-cache");
        servletResponse.setDateHeader("Expires", 0);
        servletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = servletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @GetMapping("/check")
    public ResponseEntity checkVerificationCode(@RequestParam String picCode, HttpServletRequest httpServletRequest) {
        String verificationCodeIn = (String) httpServletRequest.getSession().getAttribute("picCode");
        httpServletRequest.getSession().removeAttribute("picCode");
        if ((StringUtils.isEmpty(verificationCodeIn)) || (!verificationCodeIn.equals(picCode))) {
            return ResponseEntity.ok(ResponseVO.failed("验证码错误，或已失效"));
        } else {
            return ResponseEntity.ok(ResponseVO.success());
        }
    }

}
