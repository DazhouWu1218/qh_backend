package com.njht.webyun.publish.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author daiguojun
 * @date 2021-07-13 22:29
 * 实现redis分布式锁，redis单节点
 */
@Configuration
public class MyRedissonConfig {

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.host}")
    private String host;

    /**
     * 注册redisClient对象
     * @return
     */
    @Bean(destroyMethod = "")
    RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}