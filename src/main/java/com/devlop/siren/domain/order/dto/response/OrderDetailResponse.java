package com.devlop.siren.domain.order.dto.response;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderDetailResponse {
  private Long orderId;
  private OrderStatus orderState;
  private String storeName;
  private String storeAddress;
  private List<OrderItemResponse> items;
  private int totalQuantity;
  private int totalAmount;

  @Builder
  public OrderDetailResponse(
      Long orderId,
      OrderStatus orderStatus,
      String storeName,
      String storeAddress,
      List<OrderItemResponse> items,
      int totalQuantity,
      int totalAmount) {
    this.orderId = orderId;
    this.orderState = orderStatus;
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.items = items;
    this.totalQuantity = totalQuantity;
    this.totalAmount = totalAmount;
  }

  public static OrderDetailResponse of(Order order) {
    List<OrderItemResponse> itemResponses =
        order.getOrderItems().stream()
            .map(
                item ->
                    new OrderItemResponse(
                        item.getId(),
                        item.getItem().getItemName(),
                        item.getQuantity(),
                        item.getCustomOption(),
                        item.getItem().getCategory()))
            .collect(Collectors.toList());

    return OrderDetailResponse.builder()
        .orderId(order.getId())
        .orderStatus(order.getStatus())
        .storeName(order.getStore().getStoreName())
        .storeAddress(order.getStore().getFullAddress())
        .items(itemResponses)
        .totalQuantity(order.getTotalQuantity())
        .totalAmount(order.getTotalOrderAmount())
        .build();
  }
}
