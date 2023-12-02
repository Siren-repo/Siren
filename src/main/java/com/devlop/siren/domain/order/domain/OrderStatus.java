package com.devlop.siren.domain.order.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
  INIT, // 주문 생성
  READY, // 제조 대기
  PREPARING, // 제조 중
  PICKUP, // 픽업 대기
  COMPLETED, // 픽업 완료
  CANCELLED; // 주문 취소

  @JsonCreator
  public OrderStatus fromString(String value) {
    return OrderStatus.valueOf(value.toUpperCase());
  }
}
