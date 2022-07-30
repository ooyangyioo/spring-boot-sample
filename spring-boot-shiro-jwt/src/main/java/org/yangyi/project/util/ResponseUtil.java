package org.yangyi.project.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.yangyi.project.vo.ResponseVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    public static void okResponse(HttpServletResponse response, ResponseVO responseVO) throws IOException {
        wrapResponse(response, HttpStatus.OK, responseVO);
    }

    public static void unauthorizedResponse(HttpServletResponse response, ResponseVO responseVO) throws IOException {
        wrapResponse(response, HttpStatus.UNAUTHORIZED, responseVO);
    }

    public static void forbiddenResponse(HttpServletResponse response, ResponseVO responseVO) throws IOException {
        wrapResponse(response, HttpStatus.FORBIDDEN, responseVO);
    }

    public static void notFoundResponse(HttpServletResponse response, ResponseVO responseVO) throws IOException {
        wrapResponse(response, HttpStatus.NOT_FOUND, responseVO);
    }

    public static void serverResponse(HttpServletResponse response, ResponseVO responseVO) throws IOException {
        wrapResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, responseVO);
    }

    private static void wrapResponse(HttpServletResponse response, HttpStatus httpStatus, ResponseVO responseVO) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(httpStatus.value());
        PrintWriter printWriter = response.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(responseVO));
        printWriter.flush();
        printWriter.close();
    }

}
