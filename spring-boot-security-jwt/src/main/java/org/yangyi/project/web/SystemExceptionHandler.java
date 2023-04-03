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
import org.yangyi.project.exception.ServiceException;

@RestControllerAdvice
@Order(101)
public class SystemExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SystemExceptionHandler.class);


    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResponseVO missingServletRequestParameterException(Exception e) {
        log.warn("用户请求异常：{}", e.getMessage());
        return ApiResponseVO.failed("参数缺失");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ApiResponseVO httpRequestMethodNotSupportedException(Exception e) {
        log.warn("用户请求异常：{}", e.getMessage());
        return ApiResponseVO.failed("请使用POST请求");
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ApiResponseVO httpMediaTypeNotSupportedException(Exception e) {
        log.warn("用户请求异常：{}", e.getMessage());
        return ApiResponseVO.failed("请使用JSON格式请求");
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ApiResponseVO badCredentialsException(Exception e) {
        log.warn(e.getMessage());
        return ApiResponseVO.failed("请重新登录");
    }

    /**
     * 由@PreAuthorize注解抛出的AccessDeniedException异常，不会被accessDeniedHandler捕获，而是会被全局异常捕获
     * 如果想要被accessDeniedHandler捕获处理，需要在WebSecurityConfig中配置 antMatchers("/xxxxx").hasRole("XXX")
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiResponseVO accessDeniedException(Exception e) {
        log.warn(e.getMessage());
        return ApiResponseVO.failed("无权限访问");
    }

    /**
     * 系统业务异常
     *
     * @param e ServiceException
     * @return 响应封装
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResponseVO BusinessException(Exception e) {
        return ApiResponseVO.failed(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResponseVO exception(Exception e) {
        log.error("系统错误", e);
        return ApiResponseVO.failed("系统错误，请联系管理员");
    }

}
