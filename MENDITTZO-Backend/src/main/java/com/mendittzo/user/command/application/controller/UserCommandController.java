package com.mendittzo.user.command.application.controller;

import com.mendittzo.common.exception.SuccessCode;
import com.mendittzo.user.command.application.dto.UserUpdateDTO;
import com.mendittzo.user.command.application.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserCommandController {

    private final UserCommandService userCommandService;

    @PutMapping
    public ResponseEntity<String> updateUser(
            @RequestParam("userId") Long userId, // 추후 Security 개발 시 제거 예정
            @RequestParam("profileImage") MultipartFile profileImage,
            @RequestParam("nickname") String nickname) throws IOException {

        /* 나중에는 요청 헤더에서 로그인중인 userId 추출하는 방식으로 바꾸겠습니다. */

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .userId(userId)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

        userCommandService.updateUser(userUpdateDTO);

        return ResponseEntity.ok(SuccessCode.USER_UPDATE_SUCCESS.getMessage());
    }
}
