package com.mendittzo.auth.command.application.controller;

import com.mendittzo.auth.command.application.dto.AccessTokenResponseDTO;
import com.mendittzo.auth.command.application.dto.RefreshTokenRequestDTO;
import com.mendittzo.auth.command.application.service.KakaoAuthService;
import com.mendittzo.auth.query.application.controller.KakaoLoginController;
import com.mendittzo.auth.query.application.dto.DebookTokenDTO;
import com.mendittzo.auth.query.application.service.TokenService;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.common.exception.SuccessCode;
import com.mendittzo.security.util.JwtUtil;
import com.mendittzo.security.util.UserUtil;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import com.mendittzo.user.query.application.dto.UserQueryResponseDTO;
import com.mendittzo.user.query.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    // 새로운 액세스 토큰 발급하기
    @PostMapping("/access-tokens")
    public ResponseEntity<AccessTokenResponseDTO> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {

        AccessTokenResponseDTO newAccessToken = kakaoAuthService.sendNewAccessToken(authorizationHeader);

        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }

    // 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {

        kakaoAuthService.logoutFromDebook(authorizationHeader);

        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }
}
