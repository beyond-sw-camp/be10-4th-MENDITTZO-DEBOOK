package com.mendittzo.user.command.application.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UserUpdateDTO {

    private Long userId;
    private String nickname;
    @Nullable
    private MultipartFile profileImage;
}
