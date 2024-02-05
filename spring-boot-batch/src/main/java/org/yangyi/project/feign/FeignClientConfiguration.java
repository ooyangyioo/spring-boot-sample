package org.yangyi.project.feign;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfiguration {

    /**
     * 创建同步请求
     */
    @Bean
    @Primary
    public AuthServiceApi syncAuthServiceApi() {
        return Feign.builder()
                .encoder(new JacksonEncoder())          // JSON编码
                .decoder(new JacksonDecoder())          // JSON解码
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .client(new OkHttpClient())
                .options(requestOptions())              // 设置超时
                .retryer(retryer())                     //设置重试
                .target(AuthServiceApi.class, "http://127.0.0.1:8001");  // 设置代理的接口以及目标地址
    }

    /**
     * 创建异步请求
     */
    @Bean
    public AuthServiceApi asyncAuthServiceApi() {
        return AsyncFeign.builder()
                .encoder(new JacksonEncoder())          // JSON编码
                .decoder(new JacksonDecoder())          // JSON解码
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .client(new OkHttpClient())
                .options(requestOptions())              // 设置超时
                .retryer(retryer())                     //设置重试
                .target(AuthServiceApi.class, "http://127.0.0.1:8001");  // 设置代理的接口以及目标地址
    }

    /**
     * 超时时间设置
     * 5s连接超时
     * 10s响应超时
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(5, TimeUnit.SECONDS, 10, TimeUnit.SECONDS, true);
    }

    /**
     * 重试配置
     * 基础重连时间500ms，最大重连时间5000ms，尝试重连次数3次（每次重连时间在基础上*1.5但不能大于最大重连时间）
     */
    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY; //  关闭重试
//        return new Retryer.Default(500, 5000, 3);
    }

}
