package com.devlop.siren.domain.stock.dto.response;

import com.devlop.siren.domain.stock.entity.Stock;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockResponse {
    private Long stockId;
    private Long itemId;
    private String itemName;
    private Long storeId;
    private String storeName;

    public static StockResponse from(Stock stock) {
        return StockResponse.builder()
                .stockId(stock.getStockId())
                .itemId(stock.getItem().getItemId())
                .itemName(stock.getItem().getItemName())
                .storeId(stock.getStore().getStoreId())
                .storeName(stock.getStore().getStoreName())
                .build();
    }
}
