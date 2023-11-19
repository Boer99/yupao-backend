package com.yupi.yupao.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties("spring.redis")
public class RedissonConfig {
    private String host;

    private String port;

    @Bean
    public RedissonClient redissionClient(){
        // 1. Create config object
        Config config = new Config();
        String redisAddress=String.format("redis://%s:%s",host,port);
        config.useSingleServer().setAddress(redisAddress).setDatabase(3);

        // 2. Create Redisson instance
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
