package com.mendittzo.security.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserUtil {

    private static JwtUtil jwtUtil;

    @Autowired
    public UserUtil(JwtUtil jwtUtil) {
        UserUtil.jwtUtil = jwtUtil;
    }

    // 현재 로그인 한 사용자의 loginId 를 가져오는 메소드
    public static Long getCurrentUserLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {

            // customUserDetails 에서 loginId 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return userDetails.getLoginId();
        }
        // 인증이 안 된 경우
        return null;
    }
}
