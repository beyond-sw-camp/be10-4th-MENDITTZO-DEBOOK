package com.mendittzo.user.command.application.service;

import com.mendittzo.auth.query.application.service.TokenService;
import com.mendittzo.user.command.domain.aggregate.Status;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCommandServiceTest {

    @InjectMocks
    private UserCommandService userCommandService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("deleteUser 테스트")
    void deleteUser() {

        Long loginId = 123456789L;
        User deleteUser = User.create(
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
        when(userRepository.findByLoginId(loginId)).thenReturn(deleteUser);

        userCommandService.deleteUser(loginId);

        verify(tokenService).deleteTokens(loginId);
        verify(userRepository).delete(deleteUser);
    }
}