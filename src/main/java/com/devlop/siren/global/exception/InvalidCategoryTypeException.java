package com.devlop.siren.global.exception;

import lombok.Getter;


public class InvalidCategoryTypeException extends RuntimeException {
    public static final String errorMessage = "적합하지 않은 카테고리 입니다";

    public InvalidCategoryTypeException() {
        super(errorMessage);
    }
}
