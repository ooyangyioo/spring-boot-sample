package com.yangyi.project.mina;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:spring-mina.xml"})
public class MinaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinaApplication.class, args);
    }

}
