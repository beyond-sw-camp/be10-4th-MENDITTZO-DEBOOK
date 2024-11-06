package com.mendittzo.auth.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccessTokenResponseDTO {

    private String accessToken;
    private Long AccessTokenExpiresIn;
}
