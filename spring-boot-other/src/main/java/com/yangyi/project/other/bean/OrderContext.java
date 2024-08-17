package com.yangyi.project.other.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderContext {

    /**
     * 订单ID
     */
    @NotBlank(message = "订单ID【orderId】不能为空")
    private String orderId;


    /**
     * 订单金额
     */
    @NotNull(message = "订单金额【payAmount】不能为空")
    private Integer payAmount;

}
