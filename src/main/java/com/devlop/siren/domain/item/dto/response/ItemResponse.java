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

    private String image;

    private Boolean isNew;

    private Boolean isBest;

    private String categoryName;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .image(item.getImage())
                .isNew(item.getIsNew())
                .isBest(item.getIsBest())
                .categoryName(item.getCategory().getCategoryName()).build();
    }

}