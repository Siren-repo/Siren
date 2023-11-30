package com.devlop.siren.domain.order.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {
  @NotNull(message = "매장 아이디가 입력되지 않았습니다")
  private Long storeId;

  @NotNull(message = "주문 아이템이 입력되지 않았습니다")
  private List<OrderItemRequest> items;

  @Builder
  public OrderCreateRequest(Long storeId, List<OrderItemRequest> items) {
    this.storeId = storeId;
    this.items = items;
  }
}
