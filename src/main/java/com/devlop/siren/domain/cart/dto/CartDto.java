package com.devlop.siren.domain.cart.dto;

import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
  private String cartIdentifier;
  private List<OrderItemRequest> orderItemRequestList;
}
