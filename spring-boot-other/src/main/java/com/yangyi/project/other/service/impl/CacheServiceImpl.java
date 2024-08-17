package com.yangyi.project.other.service.impl;

import com.yangyi.project.other.bean.Person;
import com.yangyi.project.other.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements CacheService {

    /**
     * cacheNames/value：缓存组件的名字，即cacheManager中缓存的名称。
     * key：缓存数据时使用的key。默认使用方法参数值，也可以使用SpEL表达式进行编写。
     * keyGenerator：和key二选一使用。
     * cacheManager：指定使用的缓存管理器。
     * condition：在方法执行开始前检查，在符合condition的情况下，进行缓存
     * unless：在方法执行完成后检查，在符合unless的情况下，不进行缓存
     * sync：是否使用同步模式。若使用同步模式，在多个线程同时对一个key进行load时，其他线程将被阻塞。
     */
    @Override
    @Cacheable(value = "test1", key = "#id", cacheManager = "caffeineCacheManager")
    public Person caffeineCache(String id) {
        System.err.println("caffeineCache");

        Person person = new Person();
        person.setId(id);
        person.setName("caffeineCache");

        return person;
    }

    @Override
    @Cacheable(value = "test1", key = "#id", cacheManager = "redisCacheManager")
    public Person redisCache(String id) {
        System.err.println("redisCache");

        Person person = new Person();
        person.setId(id);
        person.setName("redisCache");

        return person;
    }
}
