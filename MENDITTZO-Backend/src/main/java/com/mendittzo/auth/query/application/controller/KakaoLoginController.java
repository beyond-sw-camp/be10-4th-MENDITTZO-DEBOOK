package com.mendittzo.auth.query.application.controller;

import com.mendittzo.auth.query.application.dto.DebookTokenDTO;
import com.mendittzo.auth.query.application.dto.KakaoLoginUrlResponseDTO;
import com.mendittzo.auth.query.application.service.KakaoLoginService;
import com.mendittzo.auth.query.application.service.TokenService;
import com.mendittzo.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoLoginController {

    private static final Logger log = LoggerFactory.getLogger(KakaoLoginController.class);
    private final KakaoLoginService kakaoLoginService;
    private final TokenService tokenService;

    // 1. 카카오 인증 서버에서 카카오 로그인 URL 리턴 받기
    @GetMapping("/public/auth/kakao-url")
    public ResponseEntity<KakaoLoginUrlResponseDTO> getKakaoLoginPage() {

        KakaoLoginUrlResponseDTO loginUrlResponse = kakaoLoginService.getKakaoLoginUrl();
        // todo: success code 등등 넘기게 바꾸기
        return new ResponseEntity<>(loginUrlResponse, HttpStatus.OK);
    }

    // 2. 카카오 로그인 인증 코드를 리턴 받은 후 액세스 토큰 요청하기
    // 2-1. 카카오 인증 서버가 http://localhost:8080/api/v1/auth/callback?code=AUTHORIZATION_CODE
    // 으로 리디렉션 하며 인증 코드를 URL 파라미터로 전송
    // 2-2. 응답 받은 인증 코드로 카카오 인증 서버에 액세스 토큰 요청
    // 3. 액세스 토큰으로 카카오 고유 사용자 id 요청
    // 4. 카카오 고유 사용자 id 로 DB 에서 서비스 사용자 정보 조회
    @GetMapping("/auth/callback")
    public void getAuthorizationCode(@RequestParam String code, HttpServletResponse response){

        try {
            DebookTokenDTO token = kakaoLoginService.kakaoLogin(code);

            if (token.getLoginId() == null) {
                throw new IllegalStateException("loginId가 null 임");
            }

            // 프론트엔드의 login-success 페이지로 리디렉트하며 쿼리 파라미터에 토큰 포함
            String redirectUrl = String.format(
                    "http://localhost:5173/login-success?accessToken=%s&refreshToken=%s",
                    token.getAccessToken(),
                    token.getRefreshToken()
            );

            response.sendRedirect(redirectUrl); // 리디렉트 수행

        } catch (Exception e) {

            log.error("getAuthorizationCode 실행 중 오류: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }



}