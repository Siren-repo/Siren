package com.devlop.siren.domain.item.dto.request;


import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.entity.DefaultOption;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.Nutrition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCreateRequest {
    @NotNull(message = "아이템 타입 혹은 카테고리가 입력되지 않았습니다.")
    private CategoryCreateRequest categoryRequest;

    @NotBlank(message = "아이템 이름이 입력되지 않았습니다.")
    private String itemName;

    @NotNull(message = "가격이 입력되지 않았습니다.")
    @Min(value = 0, message = "가격은 0원 이상이여야합니다.")
    private Integer price;

    @NotBlank(message = "상품 설명이 입력되지않았습니다.")
    private String description;

    private String image;

    private Boolean isBest;

    private Boolean isNew;

    @NotNull(message = "아이템 default 옵션이 입력되지않았습니다.")
    private DefaultOptionCreateRequest defaultOptionRequest;

    private String allergy;

    @NotNull(message = "아이템 영양정보 옵션이 입력되지 않았습니다.")
    private NutritionCreateRequest nutritionCreateRequest;


    public static Item toEntity(ItemCreateRequest itemCreateRequest, Category category, DefaultOption defaultOption, EnumSet<AllergyType> allergies, Nutrition nutrition) {
        return Item.builder()
                .itemName(itemCreateRequest.itemName)
                .price(itemCreateRequest.price)
                .description(itemCreateRequest.description)
                .image(itemCreateRequest.image)
                .isBest(itemCreateRequest.isBest)
                .isNew(itemCreateRequest.isNew)
                .allergies(allergies)
                .category(category)
                .defaultOption(defaultOption)
                .nutrition(nutrition)
                .build();
    }


}
