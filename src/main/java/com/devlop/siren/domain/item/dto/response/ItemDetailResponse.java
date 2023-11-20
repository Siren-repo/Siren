package com.devlop.siren.domain.item.dto.response;

import com.devlop.siren.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ItemDetailResponse {

    private Long itemId;

    private String itemName;

    private String description;

    private Integer price;

    private Boolean isNew;

    private Boolean isBest;

    private String image;

    private String allergy;

    private DefaultOptionResponse defaultOptionResponse;

    public static ItemDetailResponse from(Item item, String allergy) {
        return ItemDetailResponse.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .description(item.getDescription())
                .price(item.getPrice())
                .isNew(item.getIsNew())
                .isBest(item.getIsBest())
                .image(item.getImage())
                .allergy(allergy)
                .defaultOptionResponse(DefaultOptionResponse.from(item.getDefaultOption())).build();
    }
}