package com.devlop.siren.global.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private Enum<?> code;
    private T data;
    private String message;

    public static <T> ApiResponse<T> createSuccess(Enum<?> code, T data) {
        return new ApiResponse<>(code, data, null);
    }

    public static ApiResponse<?> createSuccess(Enum<?> code) {
        return new ApiResponse<>(code, null, null);
    }

    // 예외 발생으로 API 호출 실패시 반환
    public static ApiResponse<?> createError(Enum<?> code ,String message) {
        return new ApiResponse<>(code, null, message);
    }

}