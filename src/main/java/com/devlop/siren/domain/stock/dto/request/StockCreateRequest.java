package com.devlop.siren.domain.stock.dto.request;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.store.domain.Store;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockCreateRequest {
  @NotNull(message = "매장 번호가 입력되지 않았습니다.")
  @Min(value = 1L, message = "매장 번호는 0 이상 이여야합니다.")
  private Long storeId;

  @NotNull(message = "아이템 번호가 입력되지 않았습니다.")
  @Min(value = 1L, message = "아이템 번호는 0 이상 이여야합니다.")
  private Long itemId;

  @NotNull(message = "재고가 입력되지 않았습니다.")
  @Min(value = 0, message = "재고는 0개 이상 이여야합니다.")
  private Integer stock;

  public static Stock toEntity(StockCreateRequest stockCreateRequest, Item item, Store store) {
    return Stock.builder().item(item).store(store).stock(stockCreateRequest.getStock()).build();
  }
}
