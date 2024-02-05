package org.yangyi.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ShiroMain {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

        }));
        SpringApplication.run(ShiroMain.class, args);
    }

    @PostConstruct
    void setDefaultTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

}
