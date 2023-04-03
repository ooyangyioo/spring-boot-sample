package org.yangyi.project.web;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseVO {

    private String code;
    private String message;
    private Object data;

    public ApiResponseVO(String code, String message) {
        this(code, message, null);
    }

    public ApiResponseVO(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ApiResponseVO success(Object data) {
        return new ApiResponseVO("1", "成功", data);
    }

    public static ApiResponseVO success(String message) {
        return new ApiResponseVO("1", message, null);
    }

    public static ApiResponseVO failed(Object data) {
        return new ApiResponseVO("0", "失败", data);
    }

    public static ApiResponseVO failed(String message) {
        return new ApiResponseVO("0", message, null);
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
