package com.daita.datn.models.entities.auth;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("RedisHas")
@Builder
public class RedisToken {
    @Id
    String jwtId;

    String accountId;

    @TimeToLive(unit = TimeUnit.SECONDS)
    Long expiredTime;
}
