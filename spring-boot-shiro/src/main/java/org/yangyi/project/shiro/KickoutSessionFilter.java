package org.yangyi.project.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yangyi.project.config.ShiroConfig;
import org.yangyi.project.domain.SysUser;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;

/**
 * 限制并发人数登录
 */
public class KickoutSessionFilter extends AccessControlFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(KickoutSessionFilter.class);

    private static final int MAXIMUM_SESSIONS_DEFAULT = 1;
    private static final boolean MAX_SESSIONS_PREVENTS_LOGIN_DEFAULT = false;

    // 当同一个账户登录达到 maximumSessions 数量是否允许登录：true，新用户无法登录，保留老用户；false，旧用户被踢出。
    private boolean maxSessionsPreventsLogin = MAX_SESSIONS_PREVENTS_LOGIN_DEFAULT;
    private int maximumSessions = MAXIMUM_SESSIONS_DEFAULT; // 同一个帐号最大会话数 默认1
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setMaxSessionsPreventsLogin(boolean maxSessionsPreventsLogin) {
        this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
    }

    public void setMaximumSessions(int maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    //  设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        //  必须和缓存配置中的缓存name一致
        this.cache = cacheManager.getCache(ShiroConfig.AUTHENTICATION_CACHE_NAME);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            // 如果没有登录，直接进行之后的流程
            return true;
        }

        Session session = subject.getSession();
        SysUser user = (SysUser) subject.getPrincipal();
        String username = user.getUsername();
        Serializable sessionId = session.getId();

        //读取缓存   没有就存入
//        Deque<Serializable> deque = cache.get(username);
//
//        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
//        //就new一个空队列，不然deque对象为空，会报空指针
//        if (deque == null) {
//            deque = new LinkedList<Serializable>();
//        }
//
//        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
//        if (!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
//            //将sessionId存入队列
//            deque.push(sessionId);
//            //将用户的sessionId队列缓存
//            cache.put(username, deque);
//        }
//
//        //如果队列里的sessionId数超出最大会话数，开始踢人
//        while (deque.size() > maxSession) {
//            Serializable kickoutSessionId = null;
//            if (kickoutAfter) { //如果踢出后者
//                kickoutSessionId = deque.removeFirst();
//                //踢出后再更新下缓存队列
//                cache.put(username, deque);
//            } else { //否则踢出前者
//                kickoutSessionId = deque.removeLast();
//                //踢出后再更新下缓存队列
//                cache.put(username, deque);
//            }
//            try {
//                //获取被踢出的sessionId的session对象
//                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
//                if (kickoutSession != null) {
//                    //设置会话的kickout属性表示踢出了
//                    kickoutSession.setAttribute("kickout", true);
//                }
//            } catch (Exception e) {//ignore exception
//            }
//        }
//
//        //如果被踢出了，直接退出，重定向到踢出后的地址
//        if (session.getAttribute("kickout") != null) {
//            //会话被踢出了
//            try {
//                //退出登录
//                subject.logout();
//            } catch (Exception e) { //ignore
//            }
//            saveRequest(servletRequest);
//
//            Map<String, String> resultMap = new HashMap<String, String>();
//            //判断是不是Ajax请求
//            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) servletRequest).getHeader("X-Requested-With"))) {
//                resultMap.put("user_status", "300");
//                resultMap.put("message", "您已经在其他地方登录，请重新登录！");
//            } else {
//                //重定向
//                WebUtils.issueRedirect(servletRequest, servletResponse, kickoutUrl);
//            }
//            return false;
//        }

        return true;
    }

}
