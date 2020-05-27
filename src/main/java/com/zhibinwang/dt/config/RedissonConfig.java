package com.zhibinwang.dt.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author zhibin.wang
 * @desc
 **/
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redisson() throws  Exception{

        Config config = Config.fromYAML(new ClassPathResource("redisson.yml").getInputStream());
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}
