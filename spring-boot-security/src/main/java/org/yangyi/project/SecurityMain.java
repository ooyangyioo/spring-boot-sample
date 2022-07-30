package org.yangyi.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableRedisHttpSession
public class SecurityMain {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));
        SpringApplication.run(SecurityMain.class, args);
    }

    @PostConstruct
    void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

}
