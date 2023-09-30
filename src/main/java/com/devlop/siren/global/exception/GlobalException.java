package com.devlop.siren.global.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMESSAGE());
        this.errorCode = errorCode;
    }
}
