package com.mendittzo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 발생"),

    // 토큰 관련 에러
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰"),
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰"),
    UNSUPPORTED_JWT(HttpStatus.BAD_REQUEST, "지원하지 않는 JWT 토큰"),
    EMPTY_JWT(HttpStatus.BAD_REQUEST, "빈 JWT 토큰"),

    // 400 에러
    NOT_MATCH_FILE_EXTENSION(HttpStatus.BAD_REQUEST,"허용되지 않은 확장자입니다." ),

    // 404 에러
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
