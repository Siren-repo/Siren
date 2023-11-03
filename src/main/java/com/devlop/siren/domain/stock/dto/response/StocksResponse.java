package com.devlop.siren.domain.stock.dto.response;

import java.util.List;

import com.devlop.siren.global.common.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StocksResponse<T> {
    private T stocks;
    private PageInfo pageInfo;
}
