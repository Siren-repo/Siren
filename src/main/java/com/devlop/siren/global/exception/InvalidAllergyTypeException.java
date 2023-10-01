package com.devlop.siren.global.exception;

import lombok.Getter;


public class InvalidAllergyTypeException extends RuntimeException {
    public static final String errorMessage = "존재하지 않는 알러지 타입입니다";


    public InvalidAllergyTypeException() {
        super(errorMessage);
    }
}
