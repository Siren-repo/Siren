package com.devlop.siren.domain.order.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {

  @NotNull(message = "주문할 아이템의 아이디를 필수로 입력해야 합니다")
  private Long itemId;

  @NotNull(message = "주문할 아이템의 테이크아웃 여부를 필수로 입력해야 합니다")
  private Boolean takeout;

  @NotNull(message = "주문할 아이템의 온도를 필수로 입력해야 합니다")
  private Boolean warm;

  @NotNull(message = "주문할 아이템의 수량을 필수로 입력해야 합니다")
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
    return Objects.equals(itemId, that.itemId)
        && Objects.equals(takeout, that.takeout)
        && Objects.equals(warm, that.warm)
        && Objects.equals(customOption, that.customOption);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, takeout, warm, customOption);
  }
}
