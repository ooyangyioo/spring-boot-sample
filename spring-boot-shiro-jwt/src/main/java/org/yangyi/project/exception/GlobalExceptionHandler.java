package org.yangyi.project.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
        if (LOGGER.isErrorEnabled())
            LOGGER.error("服务器异常:", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseVO("0", "服务器异常，请联系管理员"));
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO("0", "不支持的请求方式"));
    }

    /**
     * 参数校验不通过
     *
     * @param exception 异常信息
     * @return 封装异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseVO> bindException(MethodArgumentNotValidException exception) {
        StringBuilder errorMsg = new StringBuilder();
        BindingResult bindingResult = exception.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsg.append(fieldError.getDefaultMessage()).append("，");
        }
        errorMsg.deleteCharAt(errorMsg.length() - 1);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO("0", errorMsg.toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseVO> constraintViolationException(ConstraintViolationException exception) {
        StringBuilder errorMsg = new StringBuilder();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            errorMsg.append(constraintViolation.getMessage()).append("，");
        });
        errorMsg.deleteCharAt(errorMsg.length() - 1);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO("0", errorMsg.toString()));
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
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseVO("0", "请求数据不正确"));
    }

}
