package com.yangyi.project.other.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 处理Controller接口不存在的情况
 * 在2.x版本中如果出现Controller接口404，并不会被DefaultHandlerExceptionResolver处理
 * 要自己开启 spring.mvc.throw-exception-if-no-handler-found=true
 * spring.web.resources.add-mappings=false 属性
 * 或者修改默认静态资源的请求模式/**该成其它
 *
 */
@Component
public class ExceptionResolverConfig implements WebMvcConfigurer {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.remove(resolvers.size() - 1) ;
        resolvers.add(new DefaultHandlerExceptionResolver() {
            @Override
            protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request,
                                                                 HttpServletResponse response, Object handler) throws IOException {
                ModelAndView mv = new ModelAndView(new View() {
                    @Override
                    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                        // 这里实现自己的逻辑
                        response.getWriter().println(ex.getMessage()) ;
                    }
                }) ;
                return mv ;
            }
        }) ;
    }
}
