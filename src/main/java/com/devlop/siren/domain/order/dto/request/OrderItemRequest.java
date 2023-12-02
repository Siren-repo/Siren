package com.devlop.siren.domain.order.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {
  @NotNull(message = "아이템 Id를 입력해야 합니다.")
  private Long itemId;
  @NotNull(message = "Take-Out 값을 입력해야 합니다.")
  private Boolean takeout;
  @NotNull(message = "Warm 값을 입력해야 합니다.")
  private Boolean warm;
  @NotNull(message = "수량을 입력해야 합니다.")
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderItemRequest that = (OrderItemRequest) o;
    return Objects.equals(itemId, that.itemId) && Objects.equals(takeout, that.takeout) && Objects.equals(warm, that.warm) && Objects.equals(customOption, that.customOption);
  }
  // quantity는 동등성 검사에서 제외한다

  @Override
  public int hashCode() {
    return Objects.hash(itemId, takeout, warm, quantity, customOption);
  }
}
