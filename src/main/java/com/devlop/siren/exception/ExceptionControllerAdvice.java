package com.devlop.siren.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionControllerAdvice {

    // RuntimeException
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponse> globalRequestException(
            final GlobalException globalException) {

        return ResponseEntity.badRequest().body(
                new ExceptionResponse(globalException.getMessage(), globalException.getErrorCode()));
    }


    // Valid Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> validRequestException(
        final MethodArgumentNotValidException methodArgumentNotValidException) {

        ObjectError objectError = methodArgumentNotValidException.getBindingResult()
            .getAllErrors().get(0);

        return ResponseEntity.badRequest().body(
            new ExceptionResponse(objectError.getDefaultMessage(),
                objectError.getObjectName().toUpperCase() + "_" +
                    Objects.requireNonNull(objectError.getCode()).toUpperCase()));
    }


    @Getter
    @AllArgsConstructor
    public static class ExceptionResponse {

        private String message;
        private String errorCode;
    }
}