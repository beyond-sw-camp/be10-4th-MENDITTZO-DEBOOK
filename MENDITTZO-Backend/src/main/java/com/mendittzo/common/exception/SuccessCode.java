package com.mendittzo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    SUCCESS(HttpStatus.OK,"OK"),
    REPORT_CREATE_SUCCESS(HttpStatus.CREATED, "신고 등록에 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
