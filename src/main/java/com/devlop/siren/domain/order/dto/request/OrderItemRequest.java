package com.devlop.siren.domain.order.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemRequest {
  private Long itemId;
  private Boolean takeout;
  private Boolean warm;
  private Integer quantity;
  private CustomOptionRequest customOption;

  @Builder
  public OrderItemRequest(
      Long itemId, Boolean takeout, Boolean warm, int quantity, CustomOptionRequest customOption) {
    this.itemId = itemId;
    this.takeout = takeout;
    this.warm = warm;
    this.quantity = quantity;
    this.customOption = customOption;
  }
}
