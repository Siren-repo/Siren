package com.devlop.siren.global.exception;

import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    //@Valid 검증 실패 시 Catch
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
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_VALID.getStatus()).body(ApiResponse.error(ResponseCode.ErrorCode.NOT_VALID.getStatus(), e.getMessage()));
    }



    //@Valid 검증 실패 시 Catch - Get
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_VALID.getStatus()).body(ApiResponse.error(ResponseCode.ErrorCode.NOT_VALID.getStatus(), e.getMessage()));

    }

    //DeletedById에서 내부적으로 생기는 예외처리
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getStatus()).body(ApiResponse.error(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> globalException(final GlobalException exception) {
        log.error("Error occurs {}", exception.toString());
        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(ApiResponse.error(exception.getErrorCode()));
    }

}
