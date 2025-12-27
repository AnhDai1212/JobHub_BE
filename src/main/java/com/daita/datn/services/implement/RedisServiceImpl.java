package com.daita.datn.services.implement;

import com.daita.datn.models.entities.auth.RedisToken;
import com.daita.datn.repositories.RedisTokenRepository;
import com.daita.datn.services.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTokenRepository redisTokenRepository;

    @Override
    public RedisToken saveToken(RedisToken token) {
        return redisTokenRepository.save(token);
    }

    @Override
    public boolean existsById(String tokenId) {
        return redisTokenRepository.existsById(tokenId);
    }

    @Override
    public Optional<RedisToken> findById(String tokenId) {
        return redisTokenRepository.findById(tokenId);
    }

    @Override
    public void deleteById(String tokenId) {
        redisTokenRepository.deleteById(tokenId);
    }
}
