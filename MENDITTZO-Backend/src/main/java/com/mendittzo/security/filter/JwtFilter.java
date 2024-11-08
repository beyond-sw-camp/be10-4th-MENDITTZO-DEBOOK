package com.mendittzo.security.filter;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// oncePerRequestFilter 를 상속 받아 doFilterInternal 을 오버라이딩 한다.
// 반드시 한 번만 실행되는 필터

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에 담긴 토큰의 유효성 판별 및 인증 객체 저장
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Authorization 헤더:{}", authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

            String token = authorizationHeader.substring(7);
            log.info("토큰:{}", token);

            try {
                // 유효한 토큰이면
                if (jwtUtil.validateToken(token)) {
                    log.info("유효한 토큰이므로 Authentication 객체 설정");
                    Authentication authentication = jwtUtil.getAuthentication(token);
                    // 인증 완료 -> 이후 인증 필터 건너 뜀
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (CustomException e) {

                if (e.getErrorCode() == ErrorCode.EXPIRED_JWT) {

                    log.info("유효한 토큰이 아님: {}, {}", e.getErrorCode(), e.getErrorCode());

                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    log.info("response에 401 설정");
                }

                return; // 필터 체인 중단
            }
        }

        // 위의 if문에 걸리지 않아 Authentication 객체가 설정되지 않으면, 다음 필터(인증 필터) 실행
        filterChain.doFilter(request, response);
        log.info("Authentication 객체 설정 안 됨. 인증 필터 실행");
    }
}
