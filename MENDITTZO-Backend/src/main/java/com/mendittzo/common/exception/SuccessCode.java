package com.mendittzo.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    SUCCESS(HttpStatus.OK,"OK");

    private final HttpStatus httpStatus;
    private final String message;
}
