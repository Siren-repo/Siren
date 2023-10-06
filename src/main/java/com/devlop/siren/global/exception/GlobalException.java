package com.devlop.siren.global.exception;

import com.devlop.siren.global.common.response.ResponseCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {


    private final ResponseCode.ErrorCode errorCode;
    public GlobalException(ResponseCode.ErrorCode errorCode) {
        super(errorCode.getMESSAGE());
        this.errorCode = errorCode;
    }
}
