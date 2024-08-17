package com.yangyi.project.other.controller;

import com.yangyi.project.other.bean.Person;
import com.yangyi.project.other.service.CacheService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Redis与Caffeine共同使用
 */
@RestController
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("cache")
    public List<Person> cache() {
        Person caffeineCache = cacheService.caffeineCache("yangyi1");
        Person redisCache = cacheService.redisCache("yangyi1");
        return Arrays.asList(caffeineCache, redisCache);
    }

}
