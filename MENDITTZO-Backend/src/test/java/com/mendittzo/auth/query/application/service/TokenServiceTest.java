package com.mendittzo.auth.query.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.time.Duration;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TokenServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }


    @Test
    @DisplayName("saveAccessToken 테스트")
    void saveAccessToken() {

        Long loginId = 123456789L;
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        int expireIn = 1516239022;
        String key = "access_token:" + loginId;

        tokenService.saveAccessToken(loginId, accessToken, expireIn);

        verify(valueOperations).set(key, accessToken, Duration.ofSeconds(expireIn));
    }

    @Test
    @DisplayName("deleteTokens 테스트: Redis에서 액세스 및 리프레시 토큰 삭제")
    void saveRefreshToken() {

        Long loginId = 123456789L;
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        int expireIn = 1516239022;
        String key = "refresh_token:" + loginId;

        tokenService.saveRefreshToken(loginId, refreshToken, expireIn);

        verify(valueOperations).set(key, refreshToken, Duration.ofSeconds(expireIn));
    }

    @Test
    @DisplayName("deleteTokens 테스트")
    void deleteTokens() {

        Long loginId = 123456789L;
        String accessTokenKey = "access_token:" + loginId;
        String refreshTokenKey = "refresh_token:" + loginId;

        tokenService.deleteTokens(loginId);

        verify(redisTemplate).delete(accessTokenKey);
        verify(redisTemplate).delete(refreshTokenKey);
    }
}