package com.cangqiong;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
class CangqiongApplicationTests {


    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void contextLoads() {
        redisTemplate.opsForValue().set("name","guoguo");

    }

}
