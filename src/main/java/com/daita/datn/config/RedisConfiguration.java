package com.daita.datn.config;

import lombok.AccessLevel;
import org.springframework.beans.factory.annotation.Value;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    String host;
    @Value("${spring.data.redis.port}")
    int port;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }
}
