package com.mendittzo.security.util;

import com.mendittzo.auth.command.application.dto.AccessTokenResponseDTO;
import com.mendittzo.auth.query.application.dto.DebookTokenDTO;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.security.service.userService;
import com.mendittzo.user.command.domain.aggregate.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final Key secretKey;
    private final com.mendittzo.security.service.userService userService;

    public JwtUtil(@Value("${token.secret}") String secretKey, userService userService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.userService = userService;
    }

    @Value(("${token.access_token_expiration_time}"))
    private String accessExpirationTime;

    @Value(("${token.refresh_token_expiration_time}"))
    private String refreshExpirationTime;

    // 액세스, 리프레시 토큰 생성하는 메소드
    public DebookTokenDTO generateToken(User user) {
        log.info("generateToken 함수 실행 - userId={}, nickname={}", user.getLoginId(), user.getNickname());

        log.info("secret Key: {}", secretKey);
        Long accessTokenExpirationTime = Long.parseLong(accessExpirationTime) * 1000L;
        Long refreshTokenExpirationTime = Long.parseLong(refreshExpirationTime) * 1000L;

        // JWT payload
        Claims claims = Jwts.claims().setSubject(user.getNickname());   // String 타입의 식별자가 필요하므로 닉네임으로 구분
        claims.put("socialLoginId", user.getLoginId()); // 소셜 로그인 사용자 고유 id 추가 저장

        // 토큰 발급
        String accessToken = Jwts.builder()
                .setClaims(claims)  // payload 에 사용자 및 추가 데이터 저장
                .setIssuedAt(new Date(System.currentTimeMillis()))   // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 암호화 알고리즘
                .compact();

        log.info("Access Token 생성 성공: {}", accessToken);

        String refreshToken = Jwts.builder()
                .setClaims(claims)  // payload 에 사용자 및 추가 데이터 저장
                .setIssuedAt(new Date(System.currentTimeMillis()))   // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 암호화 알고리즘
                .compact();

        log.info("Refresh Token 생성 성공: {}", refreshToken);

        return DebookTokenDTO.create(
                accessToken,
                System.currentTimeMillis() + accessTokenExpirationTime,
                refreshToken,
                System.currentTimeMillis() + refreshTokenExpirationTime,
                user.getLoginId()
        );
    }

    // 액세스 토큰만 새로 생성하는 메소드
    public AccessTokenResponseDTO generateAccessToken(User user) {
        Long accessTokenExpirationTime = Long.parseLong(accessExpirationTime) * 1000L;

        // JWT payload
        Claims claims = Jwts.claims().setSubject(user.getNickname());
        claims.put("socialLoginId", user.getLoginId());

        // 토큰 발급
        String accessToken = Jwts.builder()
                .setClaims(claims)  // payload 에 사용자 및 추가 데이터 저장
                .setIssuedAt(new Date(System.currentTimeMillis()))   // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))  // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, secretKey)  // 암호화 알고리즘
                .compact();

        log.info("새 Access Token 생성 성공: {}", accessToken);

        return new AccessTokenResponseDTO(
                accessToken,
                System.currentTimeMillis() + accessTokenExpirationTime
        );
    }

    // 토큰 검증하는 메소드
    public boolean validateToken(String token) {
        try {
            // 토큰 생성 시 사용한 키로 검증
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {

            log.info("유효하지 않은 JWT 토큰 {}", e);
            throw new CustomException(ErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException e) {

            log.info("만료된 JWT 토큰 {}", e);
            throw new CustomException(ErrorCode.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {

            log.info("지원하지 않는 JWT 토큰 {}", e);
            throw new CustomException(ErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {

            log.info("빈 JWT 토큰 claims {}", e);
            throw new CustomException(ErrorCode.EMPTY_JWT);
        }
    }

    // 토큰에서 Claims 추출하는 메소드
    public Claims parseClaims(String token) {

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    // 토큰에서 사용자의 소셜 로그인 고유 id 추출
    public Long getLoginId(String token) {

        return parseClaims(token).get("socialLoginId", Long.class);
    }

    // API 요청 헤더에 담긴 액세스 토큰으로 인증 객체(Authentication) 추출
    public Authentication getAuthentication(String token) {

        UserDetails userDetails = userService.loadUserByLoginId(this.getLoginId(token));

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
