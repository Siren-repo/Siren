package com.devlop.siren.domain.item.dto.response;

import com.devlop.siren.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {

  private Long itemId;

  private String itemName;

  private Integer price;

  private Boolean isNew;

  private Boolean isBest;

  private String image;

  private String categoryName;

  public static ItemResponse from(Item item) {
    return ItemResponse.builder()
        .itemId(item.getItemId())
        .itemName(item.getItemName())
        .price(item.getPrice())
        .isNew(item.getIsNew())
        .isBest(item.getIsBest())
        .image(item.getImage())
        .categoryName(item.getCategory().getCategoryName())
        .build();
  }
}
