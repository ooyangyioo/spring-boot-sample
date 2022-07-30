package org.yangyi.project.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.yangyi.project.vo.ResponseVO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseVO<Void>> exception(Exception e) {
        LOGGER.error("系统错误，错误信息：", e);
        ResponseVO<Void> responseVO = new ResponseVO<>("0", "系统错误", null);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseVO, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ResponseVO<Void>> accessDeniedException(AccessDeniedException e) {
        ResponseVO<Void> responseVO = new ResponseVO<>("0", "无权限访问", null);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseVO, httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity MissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        ResponseVO<Void> responseVO = new ResponseVO<>("0", "参数缺失", null);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseVO, httpHeaders, HttpStatus.BAD_REQUEST);
    }

}
