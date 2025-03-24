package com.practice.oauth2_prac.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String KEY_PREFIX = "refresh:";

    public void save(Long userId, String token, long expirationMillis) {
        String key = KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, Duration.ofMillis(expirationMillis));
    }

    public Optional<String> get(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + userId));
    }

    public void delete(Long userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }

    public boolean isValid(Long userId, String token) {
        return get(userId).map(saved -> saved.equals(token)).orElse(false);
    }
}
