package org.yangyi.project.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    public static void okResponse(HttpServletResponse response, ResponseVO<Object> responseVO) {
        wrapResponse(response, HttpStatus.OK, responseVO);
    }

    public static void unauthorizedResponse(HttpServletResponse response, ResponseVO<Object> responseVO) {
        wrapResponse(response, HttpStatus.UNAUTHORIZED, responseVO);
    }

    public static void forbiddenResponse(HttpServletResponse response, ResponseVO<Object> responseVO) {
        wrapResponse(response, HttpStatus.FORBIDDEN, responseVO);
    }

    public static void methodNotAllowedResponse(HttpServletResponse response, ResponseVO<Object> responseVO) {
        wrapResponse(response, HttpStatus.METHOD_NOT_ALLOWED, responseVO);
    }

    public static void notFoundResponse(HttpServletResponse response, ResponseVO<Object> responseVO) {
        wrapResponse(response, HttpStatus.NOT_FOUND, responseVO);
    }

    public static void serverErrorResponse(HttpServletResponse response, ResponseVO<Object> responseVO) {
        wrapResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, responseVO);
    }

    private static void wrapResponse(HttpServletResponse response, HttpStatus httpStatus, ResponseVO<Object> responseVO) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(httpStatus.value());
        PrintWriter printWriter = null;
        try {
            printWriter = response.getWriter();
            printWriter.write(new ObjectMapper().writeValueAsString(responseVO));
        } catch (Exception ignored) {
        } finally {
            printWriter.flush();
            printWriter.close();
        }
    }

}
