package com.devlop.siren.global.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> error(ResponseCode.ErrorCode errorCode){
        return new ApiResponse<>(errorCode.getStatus(), errorCode.name(), null);
    }
    public static <T> ApiResponse<T> error(HttpStatus status, String message){
        return new ApiResponse<>(status, message, null);
    }
    public static <T> ApiResponse<T> ok(ResponseCode.Normal normal, T data) {
        return new ApiResponse<>(normal.getStatus(), normal.getMESSAGE(), data);
    }
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(HttpStatus.OK, HttpStatus.OK.name(), null);
    }
}