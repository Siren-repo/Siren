package com.devlop.siren.domain.stock.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StocksResponse {
    private List<StockResponse> stocks;
}
