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
import javax.servlet.http.HttpServletResponse;

@RestController
public class GlobalErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorController.class);

    @RequestMapping(value = "/error")
    public ResponseEntity error(HttpServletRequest request, HttpServletResponse response) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if ((Integer) status == 404) {
            return new ResponseEntity<>(ResponseVO.failed("请确认访问地址是否正确！"), HttpStatus.NOT_FOUND);
        } else {
            LOGGER.error("错误码：{}，错误信息：{}", status, message);
            return new ResponseEntity("系统错误", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
