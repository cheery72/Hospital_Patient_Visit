package com.hdjunction.task.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_HOSPITAL(-1000,"존재하지 않는 병원 입니다.",HttpStatus.NOT_FOUND);


    ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
