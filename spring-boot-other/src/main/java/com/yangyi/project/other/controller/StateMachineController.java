package com.yangyi.project.other.controller;

import com.yangyi.project.other.bean.OrderContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class StateMachineController {

    /**
     * ModelAttribute注解，用来接收get请求参数，将get参数包装成实体类
     * "<a href="http://localhost:8080/applyOrder?orderId=abc123456&payAmount=100">...</a>"
     */
    @GetMapping("/applyOrder")
    public String applyOrder(@Valid @ModelAttribute OrderContext orderContext) {
        System.err.println(orderContext.getOrderId());
        System.err.println(orderContext.getPayAmount());
        return "ApplyOrder";
    }

    @GetMapping("/payOrder")
    public String payOrder() {
        return "PayOrder";
    }

}
