package com.devlop.siren.domain.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateRequest { // 단건 주문
    @NotNull(message = "매장 아이디가 입력되지 않았습니다")
    private Long storeId;

    @NotNull(message = "주문아이템이 입력되지 않았습니다")
    private List<OrderItemRequest> items;

    //TODO :: 결제 카드정보 추가 필요

    public OrderCreateRequest(Long storeId, List<OrderItemRequest> item) {
        this.storeId = storeId;
        this.items = item;
    }

}
