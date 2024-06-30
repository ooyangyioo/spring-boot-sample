package com.yangyi.project.mina.config;

import com.yangyi.project.mina.MinaCodeFactory;
import com.yangyi.project.mina.MinaHandler;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.integration.beans.InetSocketAddressEditor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyEditor;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class MinaConfig {

//    @Bean
//    public CustomEditorConfigurer customEditorConfigurer() {
//        Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>();
//        customEditors.put(SocketAddress.class, InetSocketAddressEditor.class);
//        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
//        customEditorConfigurer.setCustomEditors(customEditors);
//        return customEditorConfigurer;
//    }

    @Bean(initMethod = "bind", destroyMethod = "unbind")
    public NioSocketAcceptor nioSocketAcceptor(MinaHandler minaHandler,
                                               DefaultIoFilterChainBuilder defaultIoFilterChainBuilder) {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
        acceptor.setDefaultLocalAddress(new InetSocketAddress(8090));
        acceptor.setReuseAddress(true);
        acceptor.setFilterChainBuilder(defaultIoFilterChainBuilder);
        // 读写通道10秒内无操作进入空闲状态
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        // 绑定逻辑处理器
        acceptor.setHandler(minaHandler);
        return acceptor;
    }

    @Bean
    public DefaultIoFilterChainBuilder defaultIoFilterChainBuilder(ExecutorFilter executorFilter,
                                                                   MdcInjectionFilter mdcInjectionFilter,
                                                                   ProtocolCodecFilter protocolCodecFilter,
                                                                   LoggingFilter loggingFilter) {
        DefaultIoFilterChainBuilder ioFilterChainBuilder = new DefaultIoFilterChainBuilder();
        Map<String, IoFilter> filters = new LinkedHashMap<>();
        filters.put("executor", executorFilter);
        filters.put("mdcInjectionFilter", mdcInjectionFilter);
        filters.put("codecFilter", protocolCodecFilter);
        filters.put("loggingFilter", loggingFilter);
        ioFilterChainBuilder.setFilters(filters);
        return ioFilterChainBuilder;
    }

    @Bean
    public ExecutorFilter executorFilter() {
        return new ExecutorFilter();
    }

    @Bean
    public MdcInjectionFilter mdcInjectionFilter() {
        return new MdcInjectionFilter(MdcInjectionFilter.MdcKey.remoteAddress);
    }

    @Bean
    public ProtocolCodecFilter protocolCodecFilter(MinaCodeFactory minaCodeFactory) {
        return new ProtocolCodecFilter(minaCodeFactory);
    }

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }

}
