package org.yangyi.project.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yangyi.project.shiro.exception.PrincipalIdNullException;
import org.yangyi.project.shiro.exception.PrincipalInstanceException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RedisCache<K, V> implements Cache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final RedisManager redisManager;
    private String keyPrefix = RedisCacheManager.DEFAULT_CACHE_KEY_PREFIX;
    private final int expire;
    private String principalIdFieldName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    public RedisCache(RedisManager redisManager, String prefix, int expire, String principalIdFieldName) {
        if (redisManager == null) {
            throw new IllegalArgumentException("RedisManager 不能为空");
        }
        this.redisManager = redisManager;

        if (prefix != null && !"".equals(prefix)) {
            this.keyPrefix = prefix;
        }

        this.expire = expire;

        if (principalIdFieldName != null) {
            this.principalIdFieldName = principalIdFieldName;
        }
    }

    @Override
    public V get(K key) throws CacheException {
        logger.info("获取缓存：{}", key);
        if (key == null) {
            return null;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            Object rawValue = redisManager.get(redisCacheKey);
            if (rawValue == null) {
                return null;
            }
            return (V) rawValue;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.info("存入缓存：{}", key);
        if (key == null) {
            return value;
        }
        try {
            String redisCacheKey = getRedisCacheKey(key);
            redisManager.set(redisCacheKey, value, expire);
            return value;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        if (key == null)
            return null;
        try {
            String redisCacheKey = getRedisCacheKey(key);
            logger.info("删除缓存：{}", redisCacheKey);
            Object rawValue = redisManager.get(redisCacheKey);
            V previous = (V) rawValue;
            redisManager.del(redisCacheKey);
            return previous;
        } catch (Exception e) {
            throw new CacheException(e);
        }
    }

    private String getRedisCacheKey(K key) {
        if (key == null)
            return null;
        String redisKey;
        if (key instanceof PrincipalCollection)
            redisKey = getRedisKeyFromPrincipalIdField(((PrincipalCollection) key).getPrimaryPrincipal());
        else if (key instanceof String)
            redisKey = key.toString();
        else
            redisKey = getRedisKeyFromPrincipalIdField(key);
        return this.keyPrefix + redisKey;
    }

    private String getRedisKeyFromPrincipalIdField(Object principalObject) {
        String redisKey;
        Method principalIdGetter = null;
        Method[] methods = principalObject.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME.equals(this.principalIdFieldName) && ("getAuthCacheKey".equals(m.getName()) || "getUserId".equals(m.getName()))) {
                principalIdGetter = m;
                break;
            }
            if (m.getName().equals("get" + this.principalIdFieldName.substring(0, 1).toUpperCase() + this.principalIdFieldName.substring(1))) {
                principalIdGetter = m;
                break;
            }
        }
        if (principalIdGetter == null) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName);
        }

        try {
            Object idObj = principalIdGetter.invoke(principalObject);
            if (idObj == null) {
                throw new PrincipalIdNullException(principalObject.getClass(), this.principalIdFieldName);
            }
            redisKey = idObj.toString();
        } catch (IllegalAccessException e) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
        } catch (InvocationTargetException e) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
        }

        return redisKey;
    }

    @Override
    public void clear() throws CacheException {
        logger.info("clear cache");
        Set<String> keys = null;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
        }
        if (keys == null || keys.size() == 0) {
            return;
        }
        for (String key : keys) {
            redisManager.del(key);
        }
    }

    @Override
    public int size() {
        Long longSize = 0L;
        try {
            longSize = redisManager.scanSize(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
        }
        return longSize.intValue();
    }

    @Override
    public Set<K> keys() {
        Set<String> keys;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get keys error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        Set<K> convertedKeys = new HashSet<>();
        for (String key : keys) {
            try {
                convertedKeys.add((K) key);
            } catch (Exception e) {
                logger.error("deserialize keys error", e);
            }
        }
        return convertedKeys;
    }

    @Override
    public Collection<V> values() {
        Set<String> keys;
        try {
            keys = redisManager.scan(this.keyPrefix + "*");
        } catch (Exception e) {
            logger.error("get values error", e);
            return Collections.emptySet();
        }

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        List<V> values = new ArrayList<>(keys.size());
        for (String key : keys) {
            V value = null;
            try {
                value = (V) redisManager.get(key);
            } catch (Exception e) {
                logger.error("deserialize values= error", e);
            }
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}
