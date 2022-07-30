package org.yangyi.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class GlobalErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);

    @RequestMapping(value = "/error")
    public ResponseEntity<ResponseVO<Void>> error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if ((Integer) status == 404) {
            return new ResponseEntity<>(new ResponseVO<>("0", "地址不存在", null), HttpStatus.NOT_FOUND);
        } else {
            LOGGER.error("错误码：{}，错误信息：{}", status, message);
            return new ResponseEntity<>(new ResponseVO<>("0", "系统错误", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
