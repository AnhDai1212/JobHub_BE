package com.daita.datn.services;

import com.daita.datn.models.entities.auth.RedisToken;

import java.util.Optional;

public interface RedisService {
    RedisToken saveToken(RedisToken token);
    boolean existsById(String tokenId);
    Optional<RedisToken> findById(String tokenId);
    void deleteById(String tokenId);
}
