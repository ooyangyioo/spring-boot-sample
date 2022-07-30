package org.yangyi.project.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * session 监听器
 */
public class ShiroSessionListener implements SessionListener {

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /**
     * 回话创建
     */
    @Override
    public void onStart(Session session) {
        System.err.println("onStart");
        sessionCount.incrementAndGet();
    }

    /**
     * 退出会话
     * 用户主动退出登录
     */
    @Override
    public void onStop(Session session) {
        System.err.println("onStop");
        sessionCount.decrementAndGet();
    }

    /**
     * 会话过期
     * 用户直接关闭浏览器，或者一直未操作造成session 过期
     */
    @Override
    public void onExpiration(Session session) {
        System.err.println("onExpiration");
        sessionCount.decrementAndGet();
    }

    /**
     * 获取活动会话数
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }

}
