package com.devlop.siren.domain.item.dto.request;

import com.devlop.siren.domain.item.entity.Nutrition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NutritionCreateRequest {
    @Min(value = 0, message = "칼로리는 0보다 작을 수 없습니다")
    private Integer calorie;
    @Min(value = 0, message = "탄수화물은 0보다 작을 수 없습니다")
    private Integer carbohydrate;
    @Min(value = 0, message = "당류는 0보다 작을 수 없습니다")
    private Integer sugars;
    @Min(value = 0, message = "나트륨은 0보다 작을 수 없습니다")
    private Integer salt;
    @Min(value = 0, message = "단백질은 0보다 작을 수 없습니다")
    private Integer protein;
    @Min(value = 0, message = "지방은 0보다 작을 수 없습니다")
    private Integer province;
    @Min(value = 0, message = "콜레스테롤은 0보다 작을 수 없습니다")
    private Integer cholesterol;
    @Min(value = 0, message = "트랜스지방은 0보다 작을 수 없습니다")
    private Integer transFat;
    @Min(value = 0, message = "카페인은 0보다 작을 수 없습니다")
    private Integer caffeine;
    @Min(value = 0, message = "포화지방은 0보다 작을 수 없습니다")
    private Integer saturatedFat;

    @Getter
    public
    enum Unit {
        GRAM("g"),
        MILLI_GRAM("mg"),
        KCAL("Kcal");

        private final String unit;

        Unit(String unit) {
            this.unit = unit;
        }
    }

    public String addUnit(Integer nutrition, Unit unit) {
        return String.valueOf(Optional.ofNullable(nutrition).orElse(0)).concat(unit.getUnit());
    }

    public static Nutrition toEntity(NutritionCreateRequest nutrition) {
        return Nutrition.builder()
                .calorie(nutrition.addUnit(nutrition.getCalorie(), NutritionCreateRequest.Unit.KCAL))
                .carbohydrate(nutrition.addUnit(nutrition.getCarbohydrate(), NutritionCreateRequest.Unit.GRAM))
                .sugars(nutrition.addUnit(nutrition.getSugars(), NutritionCreateRequest.Unit.GRAM))
                .salt(nutrition.addUnit(nutrition.getSalt(), NutritionCreateRequest.Unit.MILLI_GRAM))
                .protein(nutrition.addUnit(nutrition.getProtein(), NutritionCreateRequest.Unit.GRAM))
                .province(nutrition.addUnit(nutrition.getProvince(), NutritionCreateRequest.Unit.GRAM))
                .cholesterol(nutrition.addUnit(nutrition.getCholesterol(), NutritionCreateRequest.Unit.MILLI_GRAM))
                .transFat(nutrition.addUnit(nutrition.getTransFat(), NutritionCreateRequest.Unit.GRAM))
                .caffeine(nutrition.addUnit(nutrition.getCaffeine(), NutritionCreateRequest.Unit.MILLI_GRAM))
                .saturatedFat(nutrition.addUnit(nutrition.getSaturatedFat(), NutritionCreateRequest.Unit.GRAM)).build();

    }
}
