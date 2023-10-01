package com.devlop.siren.global.exception;

import lombok.Getter;


public class InvalidSizeTypeException extends RuntimeException{
    public static final String errorMessage = "적합하지 않은 컵 사이즈 입니다";

    public InvalidSizeTypeException() {
        super(errorMessage);
    }
}
