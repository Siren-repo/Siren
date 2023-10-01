package com.devlop.siren.global.exception;

import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);


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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.NOT_VALID, errMessage.toString()));

    }


    //@Valid 검증 실패 시 Catch - Get
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        StringBuilder errMessage = new StringBuilder();
        logger.info("errMsg ### handleBindException");
        for (FieldError error : result.getFieldErrors()) {
            errMessage.append("[")
                    .append(error.getField())
                    .append("] ")
                    .append(":")
                    .append(error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.NOT_VALID, errMessage.toString()));

    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        logger.info("errMsg ### handleConstraintViolationException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.NOT_VALID, e.getMessage()));

    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateEntityException(DuplicateEntityException e) {
        logger.info("errMsg ### handleDuplicateEntityException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.DUPLICATE_CATEGORY, e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.info("errMsg ### handleEntityNotFoundException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(InvalidCategoryTypeException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidCategoryException(InvalidCategoryTypeException e) {
        logger.info("errMsg ### handleInvalidCategoryException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.INVALID_CATEGORY_TYPE, e.getMessage()));
    }

    @ExceptionHandler(InvalidAllergyTypeException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidAllergyTypeException(InvalidAllergyTypeException e) {
        logger.info("errMsg ### handleInvalidAllergyTypeException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.INVALID_ALLERGY_TYPE, e.getMessage()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResponse<?>> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        logger.info("errMsg ### handleEmptyResultDataAccessException");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(ResponseCode.Error.NOT_FOUND, e.getMessage()));
    }


}
