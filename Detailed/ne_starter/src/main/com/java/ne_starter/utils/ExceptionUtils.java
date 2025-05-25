package com.java.ne_starter.utils;

import com.java.ne_starter.dtos.response.ApiResponse;
import com.java.ne_starter.exceptions.BadRequestException;
import com.java.ne_starter.exceptions.ConflictException;
import com.java.ne_starter.exceptions.InternalServerErrorException;
import com.java.ne_starter.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class ExceptionUtils {
    public static ResponseEntity<ApiResponse<Object>> handleResponseException(Exception e) {
        if (e instanceof NotFoundException) {
            return ApiResponse.error(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
        if (e instanceof ConflictException) {
            return ApiResponse.error(
                    e.getMessage(),
                    HttpStatus.CONFLICT,
                    null
            );
        } else if (e instanceof InternalServerErrorException) {
            return ApiResponse.error(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        } else if (e instanceof BadRequestException || e instanceof InternalAuthenticationServiceException || e instanceof BadCredentialsException || e instanceof AccessDeniedException) {
            return ApiResponse.error(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        } else {
            return ApiResponse.error(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null
            );
        }
    }
}

