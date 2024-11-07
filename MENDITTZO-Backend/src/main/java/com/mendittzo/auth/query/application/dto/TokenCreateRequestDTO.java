package com.mendittzo.auth.query.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// todo: 이 파일 + 관련된 거 삭제 (redis 로 교체)

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenCreateRequestDTO {

    private Long tokenId;

    @NotNull
    private Long loginId;

    @NotNull
    private String accessToken;

    @NotNull
    private Long accessTokenExpireDatetime;

    @NotNull
    private String refreshToken;

    @NotBlank
    private Long refreshTokenExpireDatetime;

}
