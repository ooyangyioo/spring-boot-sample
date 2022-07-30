package org.yangyi.project.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

public class RedisSessionDAO extends AbstractSessionDAO {

    private static final String DEFAULT_SESSION_KEY_PREFIX = "shiro:session:";
    private static final long DEFAULT_SESSION_IN_MEMORY_TIMEOUT = 1000L;
    private static final boolean DEFAULT_SESSION_IN_MEMORY_ENABLED = true;
    private static final int DEFAULT_EXPIRE = -2;
    private static final int NO_EXPIRE = -1;
    private static final int MILLISECONDS_IN_A_SECOND = 1000;

    private static final Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    private String keyPrefix = DEFAULT_SESSION_KEY_PREFIX;
    private long sessionInMemoryTimeout = DEFAULT_SESSION_IN_MEMORY_TIMEOUT;
    private boolean sessionInMemoryEnabled = DEFAULT_SESSION_IN_MEMORY_ENABLED;
    private int expire = DEFAULT_EXPIRE;
    private RedisManager redisManager;
    private static final ThreadLocal<Map<Serializable, SessionInMemory>> sessionsInThread = new ThreadLocal();

    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid())
            return;
        if (this.sessionInMemoryEnabled)
            this.removeExpiredSessionInMemory();
        this.saveSession(session);
        if (this.sessionInMemoryEnabled)
            this.setSessionToThreadLocal(session.getId(), session);
    }

    private void saveSession(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            logger.info("Session或SessionId为空");
            throw new UnknownSessionException("Session或SessionId为空");
        }
        String key = getRedisSessionKey(session.getId());
        if (expire == DEFAULT_EXPIRE) {
            redisManager.set(key, session, (int) (session.getTimeout() / MILLISECONDS_IN_A_SECOND));
            return;
        }
        if (expire != NO_EXPIRE && expire * MILLISECONDS_IN_A_SECOND < session.getTimeout()) {
            logger.warn("Redis session expire time: "
                    + (expire * MILLISECONDS_IN_A_SECOND)
                    + " is less than Session timeout: "
                    + session.getTimeout()
                    + " . It may cause some problems.");
        }
        redisManager.set(key, session, expire);
    }

    @Override
    public void delete(Session session) {
        if (this.sessionInMemoryEnabled)
            this.removeExpiredSessionInMemory();
        if (session == null || session.getId() == null) {
            logger.error("Session或SessionId为空");
            return;
        }
        if (this.sessionInMemoryEnabled)
            this.delSessionFromThreadLocal(session.getId());
        try {
            redisManager.del(getRedisSessionKey(session.getId()));
        } catch (Exception e) {
            logger.error("删除Session异常，SessionId：[{}]", session.getId());
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
        if (this.sessionInMemoryEnabled)
            this.removeExpiredSessionInMemory();
        Set<Session> sessions = new HashSet<>();
        try {
            Set<String> keys = redisManager.scan(this.keyPrefix + "*");
            if (keys != null && keys.size() > 0) {
                for (String key : keys) {
                    Session s = (Session) redisManager.get(key);
                    sessions.add(s);
                }
            }
        } catch (Exception e) {
            logger.error("get active sessions error.");
        }
        return sessions;
    }

    @Override
    protected Serializable doCreate(Session session) {
        if (this.sessionInMemoryEnabled)
            this.removeExpiredSessionInMemory();
        if (session == null) {
            logger.error("Session为空");
            throw new UnknownSessionException("Session为空");
        }
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (this.sessionInMemoryEnabled)
            this.removeExpiredSessionInMemory();
        if (sessionId == null) {
            logger.warn("Session Id 为空");
            return null;
        }
        if (this.sessionInMemoryEnabled) {
            Session session = getSessionFromThreadLocal(sessionId);
            if (session != null)
                return session;
        }
        Session session = null;
        try {
            String sessionRedisKey = getRedisSessionKey(sessionId);
            logger.info("从Redis中获取Session：[{}]", sessionRedisKey);
            session = (Session) redisManager.get(sessionRedisKey);
            if (this.sessionInMemoryEnabled)
                setSessionToThreadLocal(sessionId, session);
        } catch (Exception e) {
            logger.error("读取Session异常. Session Id: [{}]", sessionId);
        }
        return session;
    }

    private void setSessionToThreadLocal(Serializable sessionId, Session session) {
        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        if (sessionMap == null) {
            sessionMap = new HashMap<>();
            sessionsInThread.set(sessionMap);
        }

        SessionInMemory sessionInMemory = new SessionInMemory();
        sessionInMemory.setCreateTime(new Date());
        sessionInMemory.setSession(session);

        sessionMap.put(sessionId, sessionInMemory);
    }

    private void delSessionFromThreadLocal(Serializable sessionId) {
        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        if (sessionMap == null)
            return;
        sessionMap.remove(sessionId);
    }

    private void removeExpiredSessionInMemory() {
        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        if (sessionMap == null)
            return;
        Iterator<Serializable> it = sessionMap.keySet().iterator();
        while (it.hasNext()) {
            Serializable sessionId = it.next();
            SessionInMemory sessionInMemory = sessionMap.get(sessionId);
            if (sessionInMemory == null) {
                it.remove();
                continue;
            }
            long liveTime = getSessionInMemoryLiveTime(sessionInMemory);
            if (liveTime > sessionInMemoryTimeout)
                it.remove();
        }
        if (sessionMap.size() == 0)
            sessionsInThread.remove();
    }

    private Session getSessionFromThreadLocal(Serializable sessionId) {
        if (sessionsInThread.get() == null)
            return null;
        Map<Serializable, SessionInMemory> sessionMap = sessionsInThread.get();
        SessionInMemory sessionInMemory = sessionMap.get(sessionId);
        if (sessionInMemory == null)
            return null;
        logger.debug("从内存中读取Session");
        return sessionInMemory.getSession();
    }

    private long getSessionInMemoryLiveTime(SessionInMemory sessionInMemory) {
        Date now = new Date();
        return now.getTime() - sessionInMemory.getCreateTime().getTime();
    }

    private String getRedisSessionKey(Serializable sessionId) {
        return this.keyPrefix + sessionId;
    }

    public Long getActiveSessionsSize() {
        Long size = 0L;
        try {
            size = redisManager.scanSize(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get active sessions error.");
        }
        return size;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public long getSessionInMemoryTimeout() {
        return sessionInMemoryTimeout;
    }

    public void setSessionInMemoryTimeout(long sessionInMemoryTimeout) {
        this.sessionInMemoryTimeout = sessionInMemoryTimeout;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public boolean getSessionInMemoryEnabled() {
        return sessionInMemoryEnabled;
    }

    public void setSessionInMemoryEnabled(boolean sessionInMemoryEnabled) {
        this.sessionInMemoryEnabled = sessionInMemoryEnabled;
    }

    public static ThreadLocal<Map<Serializable, SessionInMemory>> getSessionsInThread() {
        return sessionsInThread;
    }
}
