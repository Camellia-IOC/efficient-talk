package com.ETGroup.EfficientTalkServer;

import com.ETGroup.EfficientTalkServer.service.redis.RedisService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EfficientTalkServerApplicationTests {
    @Resource
    private RedisService redisService;
    
    @Test
    void contextLoads() {
    }
}
