package com.yangyi.project.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * I/O数据读写处理类
 * 使用单例的ChannelHandler添加到ChannelPipeline中那么就需要用@Sharable进行修饰
 */
@Component
@ChannelHandler.Sharable
public class NettyChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * 注册时执行
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("--channelRegistered--" + ctx.channel().id().toString());
    }

    /**
     * 离线时执行
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("--channelUnregistered--" + ctx.channel().id().toString());
    }

    /**
     * 从客户端收到新的数据时，这个方法会在收到消息时被调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg)
            return;

        String stringMessage = (String) msg;
        System.err.println(stringMessage);

        ctx.writeAndFlush(Unpooled.buffer().writeBytes(("Bye--->" + msg).getBytes()));
    }

    /**
     * 从客户端收到新的数据、读取完成时调用
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        ctx.flush();
    }

    /**
     * 当出现 Throwable 对象才会被调用，即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();//抛出异常，断开与客户端的连接
    }

    /**
     * 客户端与服务端第一次建立连接时 执行
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        //此处不能使用ctx.close()，否则客户端始终无法与服务端建立连接
        System.out.println("channelActive:"+clientIp+ctx.name());
    }

    /**
     * 客户端与服务端 断连时 执行
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        System.out.println("channelInactive:"+clientIp);
        ctx.close(); //断开连接时，必须关闭，否则造成资源浪费，并发量很大情况下可能造成宕机
    }

    /**
     * 服务端当read超时, 会调用这个方法
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        IdleStateEvent event = (IdleStateEvent) evt;
        String eventDesc = null;
        switch (event.state()) {
            case READER_IDLE:
                eventDesc = "读空闲";
                break;
            case WRITER_IDLE:
                eventDesc = "写空闲";
                break;
            case ALL_IDLE:
                eventDesc = "读写空闲";
                break;
        }
        System.out.println(ctx.channel().remoteAddress() + "发生超时事件--" + eventDesc);

        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        ctx.close(); // 超时时断开连接
        System.out.println("userEventTriggered:" + clientIp);
    }
}
