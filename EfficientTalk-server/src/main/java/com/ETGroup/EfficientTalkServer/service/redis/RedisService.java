package com.ETGroup.EfficientTalkServer.service.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
