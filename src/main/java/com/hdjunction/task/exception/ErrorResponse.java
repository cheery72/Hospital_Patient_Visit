package com.hdjunction.task.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ErrorResponse {

    private Integer code;
    private String message;
    private HttpStatus httpStatus;

    public ErrorResponse(ErrorCode e) {
        this.code = e.getCode();
        this.message = e.getMessage();
        this.httpStatus = e.getHttpStatus();
    }
}
