package com.mendittzo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    // 200
    SUCCESS(HttpStatus.OK,"OK"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "유저 정보 수정에 성공하였습니다."),

    // 201
    REPORT_CREATE_SUCCESS(HttpStatus.CREATED, "신고 등록에 성공하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
