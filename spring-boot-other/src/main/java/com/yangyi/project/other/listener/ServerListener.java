package com.yangyi.project.other.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class ServerListener implements ApplicationListener<ServletRequestHandledEvent> {

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        System.err.println("========================================") ;
        System.err.printf("请求客户端地址：%s\n请求URL: %s\n请求Method: %s\n请求耗时: %d毫秒%n",
                event.getClientAddress(),
                event.getRequestUrl(),
                event.getMethod(),
                event.getProcessingTimeMillis());
        System.err.println("========================================") ;
    }

}
