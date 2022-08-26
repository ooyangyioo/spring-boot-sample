package org.yangyi.project.exception;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.yangyi.project.vo.ResponseVO;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 其余异常
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseVO> exceptionHandler(Exception exception) {
        exception.printStackTrace();
        if (LOGGER.isErrorEnabled())
            LOGGER.error("服务器异常:", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseVO.failed("服务器异常！"));
    }

    /**
     * 请求方式不正确
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseVO> methodNotSupportedException(Exception exception) {
        if (LOGGER.isWarnEnabled())
            LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.failed("不支持的请求方式！"));
    }

    /**
     * 参数校验不通过
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseVO> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder errorMsg = new StringBuilder();
        BindingResult bindingResult = exception.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsg.append(fieldError.getDefaultMessage()).append("，");
        }
        errorMsg.deleteCharAt(errorMsg.length() - 1);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.failed(errorMsg.toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseVO> constraintViolationException(ConstraintViolationException exception) {
        StringBuilder errorMsg = new StringBuilder();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            errorMsg.append(constraintViolation.getMessage()).append("，");
        });
        errorMsg.deleteCharAt(errorMsg.length() - 1);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.failed(errorMsg.toString()));
    }

    /**
     * 未传请求数据
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseVO> messageNotReadableException(HttpMessageNotReadableException exception) {
        if (LOGGER.isWarnEnabled())
            LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseVO.failed("请求数据不正确"));
    }

    /**
     * Shiro 未授权
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseVO> unauthorizedException(UnauthorizedException exception) {
        if (LOGGER.isWarnEnabled())
            LOGGER.warn(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseVO.failed("无权限访问！"));
    }

    /**
     * 主键重复
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ResponseVO> duplicateKeyException(DuplicateKeyException exception) {
        if (LOGGER.isWarnEnabled())
            LOGGER.error("主键重复错误：{}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseVO.failed("服务器处理错误！"));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity missingServletRequestPartException(MissingServletRequestPartException exception) {
        if (LOGGER.isWarnEnabled())
            LOGGER.error("参数丢失：{}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseVO.failed("参数丢失错误！"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity dataIntegrityViolationException(DataIntegrityViolationException exception) {
        if (LOGGER.isWarnEnabled())
            LOGGER.error("执行SQL异常：{}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseVO.failed("服务器处理错误！"));
    }


}
