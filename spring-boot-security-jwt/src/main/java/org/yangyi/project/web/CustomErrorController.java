package org.yangyi.project.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yangyi.project.web.ResponseVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    @ResponseBody
    public ResponseEntity error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        //  Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        HttpStatus httpStatus = null;
        String message = "";
        switch (statusCode) {
            case 404:
                httpStatus = HttpStatus.NOT_FOUND;
                message = "请求不存在！";
                break;
            case 500:
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                message = "预期外错误！";
                break;
        }
        ResponseVO responseVO = new ResponseVO("0", message);
        return new ResponseEntity(responseVO, httpStatus);
    }

}
