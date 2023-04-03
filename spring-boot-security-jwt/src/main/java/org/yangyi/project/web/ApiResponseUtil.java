package org.yangyi.project.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ApiResponseUtil {

    public static void okResponse(HttpServletResponse response, ApiResponseVO apiResponseVO) throws IOException {
        wrapResponse(response, HttpStatus.OK, apiResponseVO);
    }

    public static void unauthorizedResponse(HttpServletResponse response, ApiResponseVO apiResponseVO) throws IOException {
        wrapResponse(response, HttpStatus.UNAUTHORIZED, apiResponseVO);
    }

    public static void forbiddenResponse(HttpServletResponse response, ApiResponseVO apiResponseVO) throws IOException {
        wrapResponse(response, HttpStatus.FORBIDDEN, apiResponseVO);
    }

    public static void notFoundResponse(HttpServletResponse response, ApiResponseVO apiResponseVO) throws IOException {
        wrapResponse(response, HttpStatus.NOT_FOUND, apiResponseVO);
    }

    public static void serverResponse(HttpServletResponse response, ApiResponseVO apiResponseVO) throws IOException {
        wrapResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, apiResponseVO);
    }

    private static void wrapResponse(HttpServletResponse response, HttpStatus httpStatus, ApiResponseVO apiResponseVO) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(httpStatus.value());
        PrintWriter printWriter = response.getWriter();
        printWriter.write(new ObjectMapper().writeValueAsString(apiResponseVO));
        printWriter.flush();
        printWriter.close();
    }

}
