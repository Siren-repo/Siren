package com.devlop.siren.domain.order.dto.response;

import static com.devlop.siren.global.common.response.ResponseCode.ErrorCode.NOT_FOUND_ORDER;

import com.devlop.siren.domain.order.domain.option.CustomOption;
import com.devlop.siren.global.exception.GlobalException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderItemResponse {
  private Long itemId;
  private String itemName;
  private Integer quantity;
  private CustomOptionResponse customOption;

  @Builder
  public OrderItemResponse(
      Long itemId, String itemName, Integer quantity, CustomOption customOption) {
    this.itemId = itemId;
    this.itemName = itemName;
    this.quantity = quantity;
    this.customOption =
        CustomOptionResponse.fromEntity(customOption)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_ORDER));
  }
}
