package com.mendittzo.user.command.application.controller;

import com.mendittzo.common.exception.SuccessCode;
import com.mendittzo.security.util.UserUtil;
import com.mendittzo.user.command.application.dto.UserUpdateDTO;
import com.mendittzo.user.command.application.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserCommandController {

    private final UserCommandService userCommandService;

    @PutMapping
    public ResponseEntity<String> updateUser(
            @RequestParam(value = "profileImage" , required = false) MultipartFile profileImage,
            @RequestParam(value = "nickname") String nickname) throws IOException {

        Long loginId = UserUtil.getCurrentUserLoginId();

        UserUpdateDTO userUpdateDTO = UserUpdateDTO.builder()
                .userId(loginId)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

        userCommandService.updateUser(userUpdateDTO);

        return ResponseEntity.ok(SuccessCode.USER_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() throws IOException {

        // 인증 객체에서 loginId 꺼내기
        Long loginId = UserUtil.getCurrentUserLoginId();

        if (loginId == null) {
            return ResponseEntity.status(401).build();
        }

        userCommandService.deleteUser(loginId);

        return ResponseEntity.ok(SuccessCode.USER_DELETE_SUCCESS.getMessage());
    }
}
