package com.devlop.siren.domain.item.dto.response;

import com.devlop.siren.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NutritionDetailResponse {
    Long itemId;
    String itemName;
    String calorie;
    String carbohydrate;
    String sugars;
    String salt;
    String protein;
    String province;
    String cholesterol;
    String transFat;
    String caffeine;
    String saturatedFat;

    public static NutritionDetailResponse from(Item item){
        return NutritionDetailResponse.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .calorie(item.getNutrition().getCalorie())
                .carbohydrate(item.getNutrition().getCarbohydrate())
                .sugars(item.getNutrition().getSugars())
                .salt(item.getNutrition().getSalt())
                .protein(item.getNutrition().getProtein())
                .province(item.getNutrition().getProvince())
                .cholesterol(item.getNutrition().getCholesterol())
                .transFat(item.getNutrition().getTransFat())
                .caffeine(item.getNutrition().getCaffeine())
                .saturatedFat(item.getNutrition().getSaturatedFat())
                .build();
    }

}
