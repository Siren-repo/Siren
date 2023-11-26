package com.devlop.siren.domain.order.dto.response;

import com.devlop.siren.domain.order.domain.Order;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateResponse {
  private Long orderId;
  private String storeName;
  private String storeAddress;
  private List<OrderItemResponse> items;
  private int totalQuantity;
  private int totalAmount;

  @Builder
  public OrderCreateResponse(
      Long orderId,
      String storeName,
      String storeAddress,
      List<OrderItemResponse> items,
      int totalQuantity,
      int totalAmount) {
    this.orderId = orderId;
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.items = items;
    this.totalQuantity = totalQuantity;
    this.totalAmount = totalAmount;
  }

  public static OrderCreateResponse of(Order order) {
    List<OrderItemResponse> itemResponses =
        order.getOrderItems().stream()
            .map(
                item ->
                    new OrderItemResponse(
                        item.getId(),
                        item.getItem().getItemName(),
                        item.getQuantity(),
                        item.getCustomOption()))
            .collect(Collectors.toList());

    return OrderCreateResponse.builder()
        .orderId(order.getId())
        .storeName(order.getStore().getStoreName())
        .storeAddress(order.getStore().getFullAddress())
        .items(itemResponses)
        .totalQuantity(order.getTotalQuantity())
        .totalAmount(order.getTotalOrderAmount())
        .build();
  }
}
