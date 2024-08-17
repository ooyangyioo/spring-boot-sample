package com.yangyi.project.other.service;

import com.yangyi.project.other.bean.Person;

public interface CacheService {

    Person caffeineCache(String id);

    Person redisCache(String id);

}
