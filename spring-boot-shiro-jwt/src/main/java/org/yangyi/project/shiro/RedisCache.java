package org.yangyi.project.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * 自定义缓存操作
 */
public class RedisCache<K, V> implements Cache<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);

    @Override
    public V get(K k) throws CacheException {
        LOGGER.info("get(K k)--->{}", k);
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        LOGGER.info("put(K k, V v)--->{},{}", k, v);
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        LOGGER.info("remove(K k)--->{}", k);
        return null;
    }

    @Override
    public void clear() throws CacheException {
        LOGGER.info("clear()");
    }

    @Override
    public int size() {
        LOGGER.info("size()");
        return 0;
    }

    @Override
    public Set<K> keys() {
        LOGGER.info("keys()");
        return null;
    }

    @Override
    public Collection<V> values() {
        LOGGER.info("values()");
        return null;
    }

}
