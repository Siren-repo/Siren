package com.devlop.siren.global.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_AUTH_ROLE(HttpStatus.UNAUTHORIZED, "매장 등록 권한이 없습니다"),
    GEOCODING_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "지오코딩 위도 경도 조회시 에러발생"),

    DUPLICATED_MEMBER(HttpStatus.CONFLICT, "이미 가입된 이메일입니다"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "가입된 유저가 아닙니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효한 패스워드가 아닙니다"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러")
    ;

    private final HttpStatus status;
    private final String message;
}
