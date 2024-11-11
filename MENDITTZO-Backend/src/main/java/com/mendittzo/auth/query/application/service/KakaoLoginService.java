package com.mendittzo.auth.query.application.service;

import com.mendittzo.auth.query.application.dto.*;
import com.mendittzo.auth.query.domain.repository.TokenRepository;
import com.mendittzo.security.util.JwtUtil;
import com.mendittzo.user.command.application.dto.UserCreateRequestDTO;
import com.mendittzo.user.command.application.service.UserCommandService;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import com.mendittzo.user.command.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private static final String KAKAO_LOGIN_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final UserRepository userRepository;
    private final UserCommandService userCommandService;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    // 필수 쿼리 파라미터
    // (1) client_id : REST API 키
    // (2) redirect_uri : 인가 코드를 전달받을 서비스 서버의 URI
    // (3) response_type : code 로 고정

    @Value("${kakao.client_id.rest_api_key}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    // 1-1. 카카오 인증 서버에 카카오 로그인 url 요청하는 메소드
    public KakaoLoginUrlResponseDTO getKakaoLoginUrl() {

        // yml 에 저장한 client_id, redirect_uri 값을  DTO 로 전달
        KakaoLoginUrlRequestDTO loginUrlRequest = new KakaoLoginUrlRequestDTO(clientId, redirectUri);

        String loginUrl = KAKAO_LOGIN_URL
                + "?client_id=" + loginUrlRequest.getClientId()
                + "&redirect_uri=" + loginUrlRequest.getRedirectUri()
                + "&response_type=code";
        return new KakaoLoginUrlResponseDTO(loginUrl);
    }

    // 2-2. 응답 받은 인증 코드로 카카오 인증 서버에 액세스 토큰 요청하는 메소드
    public String requestAccessToken(String code) {

        // WebClient : 서버에서 외부 API 로 요청 보낼 때 사용
        WebClient webClient = WebClient.create();

        // 액세스 토큰 요청
        KakaoTokenResponseDTO tokenResponse = webClient.post()
                .uri(KAKAO_TOKEN_URI)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        // 토큰이 정상적으로 응답되지 않은 경우 예외 처리
        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new RuntimeException("카카오 인증 서버에서 액세스 토큰을 받을 수 없습니다.");
        }

        return tokenResponse.getAccessToken();
    }

    // 3-1. 카카오 인증 서버에서 로그인 한 카카오 사용자 정보 요청하는 메소드
    public KakaoUserInfoResponseDTO getKakaoUserInfo(String accessToken) {

        // WebClient : 서버에서 외부 API 로 요청 보낼 때 사용
        WebClient webClient = WebClient.create();

        KakaoUserInfoResponseDTO userInfo = webClient.get()
                .uri(KAKAO_USER_INFO_URI)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDTO.class) // KakaoUserInfoResponseDTO 타입으로 응답 받기
                .block();
        if (userInfo == null) {
            throw new RuntimeException("카카오 인증 서버에서 카카오 사용자 고유 id 받아오기 실패");
        }
        return userInfo;
    }

    private User findOrCreateUser(KakaoUserInfoResponseDTO kakaoUserInfo) {

        // 카카오 로그인 한 사용자가 DB에 있는지 조회
        User existsUser = userRepository.findByLoginIdAndAuthProvider(
                kakaoUserInfo.getLoginId(), "KAKAO");

        // DB에 사용자가 없으면 생성
        if (existsUser == null) {
            UserCreateRequestDTO userRequestDTO = new UserCreateRequestDTO(
                    null,
                    null,
                    userCommandService.generateUserNickname(), // 닉네임 랜덤 생성
                    null,
                    "KAKAO",
                    null,
                    LocalDateTime.now(),
                    null,
                    kakaoUserInfo.getLoginId()
            );


            User newUser = UserMapper.toEntity(userRequestDTO);

            return userRepository.save(newUser);
        }
        return existsUser;
    }

    @Transactional
    public DebookTokenDTO kakaoLogin(String code) {

        // 1. 인증 코드로 액세스 토큰 요청
        String accessToken = requestAccessToken(code);

        // 2. 액세스 토큰으로 카카오 고유 사용자 id 요청
        KakaoUserInfoResponseDTO kakaoUserInfo = getKakaoUserInfo(accessToken);

        Long loginId = kakaoUserInfo.getLoginId();

        // 3. 카카오 고유 사용자 id 로 DB 에서 서비스 사용자 조회 및 저장
        User user = findOrCreateUser(kakaoUserInfo);

        // 4. access token, refresh token 생성
        DebookTokenDTO token = jwtUtil.generateToken(user);

        // 5. redis 에 토큰 저장
        tokenService.saveAccessToken(user.getLoginId(), token.getAccessToken(), token.getAccessTokenExpiresIn().intValue());
        tokenService.saveRefreshToken(user.getLoginId(), token.getRefreshToken(), token.getRefreshTokenExpiresIn().intValue());

        return token;
    }
}
