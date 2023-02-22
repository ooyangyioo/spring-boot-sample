package org.yangyi.project.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() //  若返回值为null，则不允许存储到Cache中
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getJackson2JsonRedisSerializer()));

        Set cacheNameSet = new HashSet() {{
            add("user");
        }};

        ConcurrentHashMap cacheConfigMap = new ConcurrentHashMap();
        cacheConfigMap.put("user", redisCacheConfiguration.entryTtl(Duration.ofSeconds(7200)));    //  自定义有效期
        //  cacheConfigMap.put("Test", redisCacheConfiguration);    //  永久有效

        RedisCacheManager cacheManager = RedisCacheManager
                .builder(redisConnectionFactory)
                .initialCacheNames(cacheNameSet)
                .withInitialCacheConfigurations(cacheConfigMap)
                .build();
        return cacheManager;
    }

    @Bean
    @Primary
    RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置自定义序列化方式
        //对字符串采取普通的序列化方式 适用于key 因为我们一般采取简单字符串作为key
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //普通的string类型的key采用 普通序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //普通hash类型的key采用 普通序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        //解决查询缓存转换异常的问题
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        //普通的值采用jackson方式自动序列化
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //hash类型的值采用jackson方式序列化
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }

    private Jackson2JsonRedisSerializer getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

}
