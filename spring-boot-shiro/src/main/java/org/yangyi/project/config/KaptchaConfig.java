//package org.yangyi.project.config;
//
//import com.google.code.kaptcha.impl.DefaultKaptcha;
//import com.google.code.kaptcha.util.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
//@Component
//public class KaptchaConfig {
//
//    @Bean
//    public DefaultKaptcha defaultKaptcha() {
//        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
//        Properties properties = new Properties();
//        // 图片边框
//        properties.setProperty("kaptcha.border", "yes");
//        // 边框颜色
//        properties.setProperty("kaptcha.border.color", "105,179,90");
//        // 字体颜色
//        properties.setProperty("kaptcha.textproducer.font.color", "red");
//        // 图片宽
//        properties.setProperty("kaptcha.image.width", "135");
//        // 图片高
//        properties.setProperty("kaptcha.image.height", "50");
//        //使用哪些字符生成验证码
//        properties.setProperty("kaptcha.textproducer.char.string", "ACEHKTW247");
//        // 字体大小
//        properties.setProperty("kaptcha.textproducer.font.size", "43");
//        // session key
//        properties.setProperty("kaptcha.session.key", "code");
//        // 验证码长度
//        properties.setProperty("kaptcha.textproducer.char.length", "4");
//        // 字体
//        properties.setProperty("kaptcha.textproducer.font.names", "Arial");
//        //干扰线颜色
//        properties.setProperty("kaptcha.noise.color", "red");
//        Config config = new Config(properties);
//        defaultKaptcha.setConfig(config);
//        return defaultKaptcha;
//    }
//
//}
