package com.yangyi.project.mina;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.FilterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MinaHandler implements IoHandler {

    private static final Logger log = LoggerFactory.getLogger(MinaHandler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        log.info("sessionCreated");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        log.info("sessionOpened");
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        log.info("sessionClosed");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        log.info("session {} ,status {}", session.getId(), status.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        log.info(String.format("message : %s", message));
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
    }

    @Override
    public void inputClosed(IoSession session) throws Exception {
        log.info("inputClosed");
        session.closeNow();
    }

    @Override
    public void event(IoSession session, FilterEvent event) throws Exception {
        log.info("session {} ,status {}", session.getId(), event.toString());
    }
}
