package com.mendittzo.auth.query.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

// redis 에 토큰을 저장, 조회, 삭제한다.

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 액세스 토큰 저장
    public void saveAccessToken(Long loginId, String accessToken, int accessTokenExpireSeconds) {

        String key = "access_token:" + loginId;
        try {
            redisTemplate.opsForValue().set(key, accessToken, Duration.ofSeconds(accessTokenExpireSeconds));
            log.info("Access Token 저장 성공: key={}, token={}", key, accessToken);

        } catch (Exception e) {
            log.error("Redis에 Access Token 저장 실패: key={}, error={}", key, e.getMessage());
        }
    }

    // 리프레시 토큰 저장
    public void saveRefreshToken(Long loginId, String refreshToken, int refreshTokenExpireSeconds) {

        String key = "refresh_token:" + loginId;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofSeconds(refreshTokenExpireSeconds));
    }

    // 액세스 토큰 조회
    public String getAccessToken(Long loginId) {

        return (String) redisTemplate.opsForValue().get("access_token:" + loginId);
    }

    // 리프레시 토큰 조회
    public String getRefreshToken(Long loginId) {

        return (String) redisTemplate.opsForValue().get("refresh_token:" + loginId);
    }

    // 액세스, 리프레시 토큰 삭제
    public void deleteTokens(Long loginId) {
        redisTemplate.delete("access_token:" + loginId);
        redisTemplate.delete("refresh_token:" + loginId);
    }
}
