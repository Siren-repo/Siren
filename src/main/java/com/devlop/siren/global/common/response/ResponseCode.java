package com.devlop.siren.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class ResponseCode {
  @Getter
  @RequiredArgsConstructor
  public enum ErrorCode {
    NOT_AUTH_ROLE(HttpStatus.UNAUTHORIZED, "매장 권한이 없습니다"),
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "매장이 존재하지 않습니다."),
    FAIL_STORE_SAVE(HttpStatus.BAD_REQUEST, "매장 등록에 실패 하였습니다."),
    WRONG_LAT_LONG_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 데이터 입니다."),
    GEOCODING_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "지오코딩 위도 경도 조회시 에러발생"),

    DUPLICATED_MEMBER(HttpStatus.CONFLICT, "이미 가입된 이메일입니다"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "가입된 유저가 아닙니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    INVALID_AUTH(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다"),
    ALREADY_LOGGED_OUT(HttpStatus.UNAUTHORIZED, "이미 로그아웃 된 계정입니다"),
    ALREADY_LOGGED_IN(HttpStatus.UNAUTHORIZED, "이미 로그인 된 계정입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효한 패스워드가 아닙니다"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),

    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "중복된 카테고리 입니다"),
    INVALID_CATEGORY_TYPE(HttpStatus.BAD_REQUEST, "적합하지 않은 카테고리 타입입니다"),
    INVALID_ALLERGY_TYPE(HttpStatus.BAD_REQUEST, "적합하지 않은 알러지 타입입니다"),
    INVALID_SIZE_TYPE(HttpStatus.BAD_REQUEST, "적합하지 않은 컵 사이즈 타입입니다"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "해당하는 카테고리를 찾을 수 없습니다"),
    NOT_FOUND_ITEM(HttpStatus.NOT_FOUND, "해당하는 아이템을 찾을 수 없습니다"),
    NOT_VALID(HttpStatus.NOT_ACCEPTABLE, "적합하지 않은 입력입니다"),
    NOT_AUTHORITY_USER(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다"),

    OUT_OF_STOCK(HttpStatus.CONFLICT, "재고가 부족하여 주문을 진행할 수 없습니다"),
    NOT_FOUND_STOCKS_IN_STORE(HttpStatus.NOT_FOUND, "해당 매장에 재고가 존재하지 않습니다."),
    NOT_FOUND_STOCK_IN_STORE(HttpStatus.NOT_FOUND, "해당 매장에 해당하는 아이템 재고가 존재하지 않습니다."),

    NOT_OPERATING_TIME(HttpStatus.FORBIDDEN, "해당 매장의 영업시간이 아닙니다"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다"),
    ALREADY_ORDERED(HttpStatus.CONFLICT, "진행중이거나 이미 완료된 주문이라 취소할 수 없습니다"),
    ALREADY_COMPLETED_ORDER(HttpStatus.CONFLICT, "이미 완료된 주문이라 변경할 수 없습니다"),
    NOT_FOUND_ITEM_IN_CART(HttpStatus.NOT_FOUND, "장바구니에서 해당하는 아이템을 찾을 수 없습니다"),
    CAUSE_ALLERGY_IN_CART(HttpStatus.BAD_REQUEST, "사용자의 알러지를 유발할 수 있는 아이템입니다.");

    private final HttpStatus status;
    private final String MESSAGE;
  }

  @RequiredArgsConstructor
  @Getter
  public enum Normal {
    UPDATE(HttpStatus.OK, "수정 되었습니다"),
    CREATE(HttpStatus.CREATED, "생성 되었습니다"),
    DELETE(HttpStatus.OK, "삭제 되었습니다"),
    RETRIEVE(HttpStatus.OK, "조회 되었습니다");
    private final HttpStatus status;
    private final String MESSAGE;
  }
}
