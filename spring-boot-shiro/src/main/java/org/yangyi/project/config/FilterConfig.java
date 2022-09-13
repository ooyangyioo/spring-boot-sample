package org.yangyi.project.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.yangyi.project.filter.LogTraceIdFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LogTraceIdFilter> logTraceIdFilter() {
        final FilterRegistrationBean<LogTraceIdFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        final LogTraceIdFilter logTraceIdFilter = new LogTraceIdFilter();
        filterRegistrationBean.setFilter(logTraceIdFilter);
        filterRegistrationBean.setName("logTraceIdFilter");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

}
