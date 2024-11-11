package com.mendittzo.security.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUtilTest {
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserUtil userUtil;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getCurrentUserLoginId 테스트: 로그인 후 인증 객체가 저장된 경우")
    void getCurrentUserLoginId1() {

        Long expectedLoginId = 123456789L;
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getLoginId()).thenReturn(expectedLoginId);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long loginId = UserUtil.getCurrentUserLoginId();

        assertEquals(expectedLoginId, loginId);
        verify(userDetails).getLoginId();
    }

    @Test
    @DisplayName("getCurrentUserLoginId 테스트: 로그인 후 인증 객체가 저장되지 않은 경우")
    void testGetCurrentUserLoginIdUnauthenticated() {

        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        Long loginId = UserUtil.getCurrentUserLoginId();

        assertEquals(null, loginId);
    }
}