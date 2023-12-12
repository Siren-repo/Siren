package com.devlop.siren.domain.order.dto.request;

import com.devlop.siren.domain.order.domain.OrderStatus;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderStatusRequest {
  @NotNull(message = "주문상태를 변경할 주문 아이디는 필수로 입력해야합니다")
  private Long orderId;

  @NotNull(message = "변경할 주문상태는 필수로 입력해야합니다")
  private OrderStatus status;

  public OrderStatusRequest(Long orderId, OrderStatus status) {
    this.orderId = orderId;
    this.status = status;
  }
}
