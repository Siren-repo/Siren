package com.devlop.siren.global.exception;

import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        StringBuilder errMessage = new StringBuilder();
        log.error("Error occurs {}", e.toString());
        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage());
        }
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_VALID.getStatus())
                .body(ApiResponse.error(ResponseCode.ErrorCode.NOT_VALID.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_VALID.getStatus())
                .body(ApiResponse.error(ResponseCode.ErrorCode.NOT_VALID.getStatus(), e.getMessage()));

    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getStatus())
                .body(ApiResponse.error(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> globalException(final GlobalException exception) {
        log.error("Error occurs {}", exception.toString());
        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(ApiResponse.error(exception.getErrorCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> applicationHandler(RuntimeException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.name()));
    }

}