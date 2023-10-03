package com.devlop.siren.global.exception;

import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
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
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);


    //@Valid 검증 실패 시 Catch
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errMessage = new StringBuilder();
        logger.info("errMsg ### handleMethodArgumentNotValid");
        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage());
        }

        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_VALID.getStatus()).body(ApiResponse.error(ResponseCode.ErrorCode.NOT_VALID.getStatus(), ex.getMessage()));

    }


    //@Valid 검증 실패 시 Catch - Get
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        logger.info("errMsg ### handleConstraintViolationException");
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_VALID.getStatus()).body(ApiResponse.error(ResponseCode.ErrorCode.NOT_VALID.getStatus(), e.getMessage()));

    }

    //DeletedById에서 내부적으로 생기는 예외처리
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        logger.info("errMsg ### handleEmptyResultDataAccessException");
        return ResponseEntity.status(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getStatus()).body(ApiResponse.error(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<?> globalException(final GlobalException exception) {
        logger.error("Error occurs {}", exception.toString());

        return ResponseEntity.status(exception.getErrorCode().getStatus())
                .body(ApiResponse.error(exception.getErrorCode()));
    }

}
