package com.devlop.siren.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_AUTH_ROLE("매장 등록 권한이 없습니다."),
    GEOCODING_PARSING_ERROR("지오코딩 위도 경도 조회시 에러발생."),
    ;

    private final String MESSAGE;
}
