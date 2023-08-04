package org.yangyi.project.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> httpMediaTypeNotAcceptableException(Exception e) {
        return new ResponseEntity<>("不支持的 MediaType", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> httpRequestMethodNotSupportedException(Exception e) {
        return new ResponseEntity<>("请使用Post方式请求", HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        logger.error("系统错误", e);
        return new ResponseEntity<>("系统错误", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
