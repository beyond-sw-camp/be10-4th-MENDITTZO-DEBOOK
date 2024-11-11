package com.mendittzo.security.service;

import com.mendittzo.security.util.CustomUserDetails;
import com.mendittzo.user.command.domain.aggregate.Status;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class userServiceTest {

    @InjectMocks
    private userService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("loadUserByLoginId 테스트: loginId 가 존재하는 경우")
    void loadUserByLoginId() {

        Long loginId = 123456789L;
        User user = User.create(
                1L,
                "bookfox@gmail.com",
                "책 먹는 여우",
                Status.ACTIVE,
                "KAKAO",
                "http://example.com/profile.jpg",
                LocalDateTime.now(),
                null,
                loginId
        );

        when(userRepository.findByLoginId(loginId)).thenReturn(user);

        UserDetails userDetails = userService.loadUserByLoginId(loginId);

        assertEquals(loginId, ((CustomUserDetails) userDetails).getLoginId());
        assertEquals(user.getUserId(), ((CustomUserDetails) userDetails).getUserId());
        assertEquals(user.getStatus(), ((CustomUserDetails) userDetails).getStatus());
    }

    @Test
    @DisplayName("loadUserByLoginId 테스트: loginId가 존재하지 않는 경우")
    void testLoadUserByLoginId() {

        Long loginId = 123456789L;

        when(userRepository.findByLoginId(loginId)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByLoginId(loginId));
    }
}