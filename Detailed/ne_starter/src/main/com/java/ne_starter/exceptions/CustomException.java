package com.java.ne_starter.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.zalando.problem.AbstractThrowableProblem;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;

    // Constructor with message and status
    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    // Constructor with message, status, and cause
    public CustomException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    // Constructor with just message (defaults to INTERNAL_SERVER_ERROR)
    public CustomException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Constructor with just cause (defaults to INTERNAL_SERVER_ERROR)
    public CustomException(Throwable cause) {
        this(cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
