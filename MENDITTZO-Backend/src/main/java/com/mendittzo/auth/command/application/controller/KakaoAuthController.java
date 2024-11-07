package com.mendittzo.auth.command.application.controller;

import com.mendittzo.auth.command.application.dto.AccessTokenResponseDTO;
import com.mendittzo.auth.command.application.dto.RefreshTokenRequestDTO;
import com.mendittzo.auth.command.application.service.KakaoAuthService;
import com.mendittzo.auth.query.application.controller.KakaoLoginController;
import com.mendittzo.auth.query.application.dto.DebookTokenDTO;
import com.mendittzo.auth.query.application.service.TokenService;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class KakaoAuthController {

    private final KakaoAuthService kakaoAuthService;

    // 새로운 액세스 토큰 발급하기
    @PostMapping("token")
    public ResponseEntity<AccessTokenResponseDTO> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {

        String refreshToken = authorizationHeader.replace("Bearer ", "");
        AccessTokenResponseDTO AccessTokenResponse = kakaoAuthService.sendNewAccessToken(new RefreshTokenRequestDTO(refreshToken));

        return new ResponseEntity<>(AccessTokenResponse, HttpStatus.OK);
    }
}
