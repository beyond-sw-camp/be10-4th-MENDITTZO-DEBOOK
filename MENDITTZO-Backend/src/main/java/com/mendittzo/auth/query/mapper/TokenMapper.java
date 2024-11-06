package com.mendittzo.auth.query.mapper;

import com.mendittzo.auth.query.application.dto.TokenCreateRequestDTO;
import com.mendittzo.auth.query.domain.aggregate.Token;

public class TokenMapper {

    public static Token toEntity(TokenCreateRequestDTO tokenRequest) {

        return Token.create(
                tokenRequest.getLoginId(),
                tokenRequest.getAccessToken(),
                tokenRequest.getAccessTokenExpireDatetime(),
                tokenRequest.getRefreshToken(),
                tokenRequest.getRefreshTokenExpireDatetime()
        );
    }

}
