package com.mendittzo.auth.command.application.service;

import com.mendittzo.auth.command.application.dto.AccessTokenResponseDTO;
import com.mendittzo.auth.command.application.dto.RefreshTokenRequestDTO;
import com.mendittzo.auth.query.application.service.TokenService;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.security.util.JwtUtil;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    // 리프레시 토큰 검증 후 새로운 액세스 토큰을 보내는 메소드
    public AccessTokenResponseDTO sendNewAccessToken(RefreshTokenRequestDTO refreshTokenRequest) {

        try {
            // 리프레시 토큰의 유효 시간 검사
            jwtUtil.validateToken(refreshTokenRequest.getRefreshToken());

            // 리프레시 토큰에서 소셜 로그인 사용자 고유 id 추출
            Long loginId = jwtUtil.getLoginId(refreshTokenRequest.getRefreshToken());

            // redis 에 저장된 리프레시 토큰과 일치하는지 확인
            String storedRefreshToken = tokenService.getRefreshToken(loginId);

            // redis 에 없거나 일치하지 않으면
            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshTokenRequest.getRefreshToken())) {
                throw new CustomException(ErrorCode.INVALID_JWT);
            }

            // 새로운 액세스 토큰 발급
            User user = userRepository.findByLoginId(loginId);
            if(user == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_USER);
            }
            AccessTokenResponseDTO newAccessToken = jwtUtil.generateAccessToken(user);

            // Redis 에 새 액세스 토큰 저장
            tokenService.saveAccessToken(loginId, newAccessToken.getAccessToken(), newAccessToken.getAccessTokenExpiresIn().intValue());

            return newAccessToken;
        } catch (Exception e) {
            log.info("예외:", e);
            throw new RuntimeException(e);
        }
    }
}
