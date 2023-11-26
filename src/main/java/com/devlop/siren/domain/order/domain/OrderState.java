package com.devlop.siren.domain.order.domain;

public enum OrderState {
  INIT, // 주문 생성
  READY, // 제조 대기
  PREPARING, // 제조 중
  PICKUP, // 픽업 대기
  COMPLETED, // 픽업 완료
  CANCELLED, // 주문 취소
}
