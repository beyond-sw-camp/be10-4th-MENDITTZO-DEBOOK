package com.mendittzo.security.config;

import com.mendittzo.security.filter.JwtFilter;
import com.mendittzo.security.handler.JwtAccessDeniedHandler;
import com.mendittzo.security.handler.JwtAuthenticationEntryPoint;
import com.mendittzo.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws  Exception {

        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz ->
                    // 일단 /api/v1/** 의 모든 요청(GET, POST...) 허락
                    authz.requestMatchers(new AntPathRequestMatcher("/api/v1/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/ws-stomp/**")).permitAll()
                            .requestMatchers("/**").permitAll()
                            .anyRequest().authenticated()   // 나머지 요청은 인증 필요
            )
            // 세션 로그인 사용 X
            .sessionManagement(
                    session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // JWT 토큰 확인 필터 설정
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            // 인증, 인가 실패 핸들러 설정
            .exceptionHandling(
                    exceptionHandling -> {
                        exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                        exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                    }
            )
            // CORS 설정
            .cors(cors -> cors
                    .configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // CORS
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        config.addExposedHeader("token"); // 서버측에서 보내는 헤더에 대한 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
