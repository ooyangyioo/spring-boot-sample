package org.yangyi.project.filter;

import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class LogTraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        beforeHandle(request);
        filterChain.doFilter(request, response);
        afterHandle();
    }

    public void beforeHandle(HttpServletRequest request) {
        String traceId = request.getHeader("TraceId");
        if (null == traceId) {
            traceId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        }
        MDC.put("TraceId", traceId);
    }

    public void afterHandle() {
        MDC.remove("TraceId");
    }

}
