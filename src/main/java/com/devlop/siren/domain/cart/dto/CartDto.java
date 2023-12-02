package com.devlop.siren.domain.cart.dto;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private String cartIdentifier;
    private List<OrderItemRequest> orderItemRequestList;
}
