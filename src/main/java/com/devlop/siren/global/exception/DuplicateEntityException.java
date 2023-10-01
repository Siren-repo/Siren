package com.devlop.siren.global.exception;

import lombok.Getter;



public class DuplicateEntityException extends RuntimeException {

    public static final String errorMessage = "이미 존재하는 엔티티입니다";

    public DuplicateEntityException() {
        super(errorMessage);
    }

}
