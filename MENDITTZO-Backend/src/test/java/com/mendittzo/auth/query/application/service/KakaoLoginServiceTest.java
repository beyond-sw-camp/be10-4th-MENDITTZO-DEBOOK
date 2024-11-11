package com.mendittzo.auth.query.application.service;

import com.mendittzo.auth.query.application.dto.KakaoLoginUrlResponseDTO;
import com.mendittzo.auth.query.application.dto.KakaoUserInfoResponseDTO;
import com.mendittzo.user.command.application.dto.UserCreateRequestDTO;
import com.mendittzo.user.command.application.service.UserCommandService;
import com.mendittzo.user.command.domain.aggregate.Status;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import com.mendittzo.user.command.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class KakaoLoginServiceTest {

    @InjectMocks
    private KakaoLoginService kakaoLoginService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCommandService userCommandService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(kakaoLoginService, "clientId", "a12375829dkbx390291234aa234ngls");
        ReflectionTestUtils.setField(kakaoLoginService, "redirectUri", "http://localhost:8080/auth/callback");
    }

    // getKakaoLoginUrl 테스트
    @Test
    void testGetKakaoLoginUrl() {

        String expectedUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=a12375829dkbx390291234aa234ngls"
                + "&redirect_uri=http://localhost:8080/auth/callback"
                + "&response_type=code";

        KakaoLoginUrlResponseDTO result = kakaoLoginService.getKakaoLoginUrl();

        assertEquals(expectedUrl, result.getKakaoLoginUrl());
    }

    // findOrCreatUser 테스트 1
    @Test
    @DisplayName("findOrCreateUser 테스트: 기존 유저가 있는 경우")
    void testFindOrCreateUser1() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        KakaoUserInfoResponseDTO kakaoUserInfo = new KakaoUserInfoResponseDTO();
        kakaoUserInfo.setLoginId(123456789L);

        User existUser = User.create(
                1L,
                "bookfox@gmail.com",
                "책 먹는 여우",
                Status.ACTIVE,
                "KAKAO",
                "http://example.com/profile.jpg",
                LocalDateTime.now(),
                null,
                123456789L
        );

        when(userRepository.findByLoginIdAndAuthProvider(kakaoUserInfo.getLoginId(), "KAKAO")).thenReturn(existUser);

        // private 메소드 접근을 위한 reflection 사용
        Method method = KakaoLoginService.class.getDeclaredMethod("findOrCreateUser", KakaoUserInfoResponseDTO.class);
        method.setAccessible(true);
        User result = (User) method.invoke(kakaoLoginService, kakaoUserInfo);

        assertEquals(existUser, result);
        verify(userRepository, never()).save(any(User.class));
    }

    // findOrCreatUser 테스트 2
    @Test
    @DisplayName("findOrCreateUser 테스트: 기존 유저가 없는 경우")
    void testFindOrCreateUser2() throws Exception {

        KakaoUserInfoResponseDTO kakaoUserInfo = new KakaoUserInfoResponseDTO();
        kakaoUserInfo.setLoginId(123456789L);

        when(userRepository.findByLoginIdAndAuthProvider(kakaoUserInfo.getLoginId(), "KAKAO")).thenReturn(null);
        when(userCommandService.generateUserNickname()).thenReturn("NewUserNickname");

        UserCreateRequestDTO userRequestDTO = new UserCreateRequestDTO(
                null,
                null,
                "NewUserNickname",
                null,
                "KAKAO",
                null,
                LocalDateTime.now(),
                null,
                123456789L
        );
        User newUser = UserMapper.toEntity(userRequestDTO);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Method method = KakaoLoginService.class.getDeclaredMethod("findOrCreateUser", KakaoUserInfoResponseDTO.class);
        method.setAccessible(true);
        User result = (User) method.invoke(kakaoLoginService, kakaoUserInfo);

        assertEquals(newUser, result); // 생성된 유저와 반환된 유저가 동일한지 확인
        verify(userRepository).save(any(User.class)); // 새로운 유저가 저장되었는지 확인
        verify(userCommandService).generateUserNickname(); // 닉네임 생성 메소드 호출 여부 확인
    }

}