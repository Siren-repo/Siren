package com.devlop.siren.domain.order.dto.response;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import lombok.Builder;

import java.util.List;

public class OrderCreateResponse {
    private Long orderId;
    private String storeName;
    private String storeAddress;
    private List<OrderItem> items;
    private int totalCnt;
    private int totalAmount;

    @Builder
    public OrderCreateResponse(Long orderId, String storeName, String storeAddress, List<OrderItem> items, int totalCnt, int totalAmount) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.items = items;
        this.totalCnt = totalCnt;
        this.totalAmount = totalAmount;
    }

    public static OrderCreateResponse of(Order order){
        return OrderCreateResponse.builder()
                .orderId(order.getId())
                .storeName(order.getStore().getStoreName())
                .storeAddress(order.getStore().getFullAddress())
                .items(order.getOrderItems())
                .build();
    }
}
