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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    // 리프레시 토큰 검증 후 새로운 액세스 토큰을 보내는 메소드
    @Transactional
    public AccessTokenResponseDTO sendNewAccessToken(String authorizationHeader){

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }

        // 리프레시 토큰 추출
        String refreshToken = authorizationHeader.substring(7);

        // 리프레시 토큰의 유효성 검증
        jwtUtil.validateToken(refreshToken);


        // 리프레시 토큰에서 사용자 정보 추출
        Long loginId = jwtUtil.getLoginId(refreshToken);
        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        // redis 에 저장된 리프레시 토큰과 일치하는지 확인
        String storedRefreshToken = tokenService.getRefreshToken(loginId);
        // redis 에 없거나 일치하지 않으면
        if (storedRefreshToken == null) {
            throw new CustomException(ErrorCode.EMPTY_JWT);
        }
        if(!storedRefreshToken.equals(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_JWT);
        }

        // 새로운 액세스 토큰 생성
        AccessTokenResponseDTO newAccessToken = jwtUtil.generateAccessToken(user);

        // redis 에 액세스 토큰 저장
        tokenService.saveAccessToken(user.getLoginId(), newAccessToken.getAccessToken(), newAccessToken.getAccessTokenExpiresIn().intValue());

        return newAccessToken;
    }

    // 로그아웃
    @Transactional
    public void logoutFromDebook(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }

        // 액세스 토큰 추출
        String accessToken = authorizationHeader.substring(7);

        // 액세스 토큰에서 사용자 정보 추출
        Long loginId = jwtUtil.getLoginId(accessToken);

        // redis 에서 액세스, 리프레시 토큰 삭제
        tokenService.deleteTokens(loginId);
        log.info("로그아웃 요청한 loginId: {} 의 액세스, 리프레시 토큰 삭제 완료", loginId);

    }
}
