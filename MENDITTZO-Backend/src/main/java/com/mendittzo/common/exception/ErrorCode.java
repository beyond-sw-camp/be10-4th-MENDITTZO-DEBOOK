package com.mendittzo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 발생"),




    // 404 에러
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저정보를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
