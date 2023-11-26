package com.devlop.siren.domain.order.dto.response;

import com.devlop.siren.domain.order.domain.option.CustomOption;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderItemResponse {
  private Long itemId;
  private String itemName;
  private Integer quantity;
  private CustomOption customOption;

  public OrderItemResponse(
      Long itemId, String itemName, Integer quantity, CustomOption customOption) {
    this.itemId = itemId;
    this.itemName = itemName;
    this.quantity = quantity;
    this.customOption = customOption;
  }
}
