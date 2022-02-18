package com.jselva.nisum.evaluationtest.data.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;

    public ApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ApiException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
