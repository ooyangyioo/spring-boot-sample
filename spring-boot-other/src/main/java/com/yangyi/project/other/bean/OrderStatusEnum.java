package com.yangyi.project.other.bean;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {

    INITIAL(0, "初始状态"),
    PAY_PENDING(1, "待支付"),
    PAY_FAILED(3, "支付失败"),
    PAY_SUCCESS(4, "已支付"),
    CANCELED(5, "已取消");

    private final int statusCode;

    private final String statusDesc;

    OrderStatusEnum(int statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

}
