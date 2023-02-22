package org.yangyi.project.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yangyi.project.exception.BusinessException;

@RestControllerAdvice
@Order(101)
public class OtherExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(OtherExceptionHandler.class);


    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseVO missingServletRequestParameterException(Exception e) {
        log.warn("用户请求异常：{}", e.getMessage());
        return ResponseVO.failed("参数缺失");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseVO httpRequestMethodNotSupportedException(Exception e) {
        log.warn("用户请求异常：{}", e.getMessage());
        return ResponseVO.failed("请使用POST请求");
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ResponseVO httpMediaTypeNotSupportedException(Exception e) {
        log.warn("用户请求异常：{}", e.getMessage());
        return ResponseVO.failed("请使用JSON格式请求");
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseVO badCredentialsException(Exception e) {
        log.warn(e.getMessage());
        return ResponseVO.failed("请重新登录");
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseVO accessDeniedException(Exception e) {
        log.warn(e.getMessage());
        return ResponseVO.failed("不允许访问");
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseVO BusinessException(Exception e) {
        return ResponseVO.failed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseVO exception(Exception e) {
        log.error("系统错误", e);
        return ResponseVO.failed("系统错误，请联系管理员");
    }

}
