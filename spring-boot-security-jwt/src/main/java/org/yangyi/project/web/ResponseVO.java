package org.yangyi.project.web;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO {

    private String code;
    private String message;
    private Object data;

    public ResponseVO(String code, String message) {
        this(code, message, null);
    }

    public ResponseVO(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseVO success(Object data) {
        return new ResponseVO("1", "成功", data);
    }

    public static ResponseVO failed(Object data) {
        return new ResponseVO("0", "失败", data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
