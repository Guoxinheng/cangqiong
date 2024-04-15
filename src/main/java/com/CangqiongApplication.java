package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching//开启缓存注解功能
@MapperScan("com.mapper")
@EnableScheduling//开启任务调度
public class CangqiongApplication {

    public static void main(String[] args) {
        SpringApplication.run(CangqiongApplication.class, args);
        System.out.println("ada");
    }

}