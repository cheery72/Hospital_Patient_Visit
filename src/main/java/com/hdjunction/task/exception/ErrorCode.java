package com.hdjunction.task.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_HOSPITAL(-1000,"존재하지 않는 병원 입니다.",HttpStatus.NOT_FOUND),
    NOT_FOUND_PATIENT(-1001,"존재하지 않는 환자 입니다.",HttpStatus.NOT_FOUND),
    FAILED_CREATE_PROCESS(-1002, "환자 생성이 실패했습니다. 잠시 후 다시 시도해주세요.",HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(Integer code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    private final Integer code;
    private final String message;
    private final HttpStatus httpStatus;
}
