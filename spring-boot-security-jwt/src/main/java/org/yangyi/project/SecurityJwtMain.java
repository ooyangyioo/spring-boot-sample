package org.yangyi.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "org.yangyi.project.dao")
public class SecurityJwtMain {
    public static void main(String[] args) {
        SpringApplication.run(SecurityJwtMain.class, args);
    }
}
