package com.devlop.siren.domain.item.entity;

import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import lombok.*;

import javax.persistence.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "nutrition")
@Getter
public class Nutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nutritionId;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String calorie;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String carbohydrate;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String sugars;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String salt;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String protein;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String province;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String cholesterol;

    @Setter
    @Column(name = "trans_fat", columnDefinition = "NVARCHAR(255)")
    private String transFat;

    @Setter
    @Column(columnDefinition = "NVARCHAR(255)")
    private String caffeine;

    @Setter
    @Column(name = "saturated_fat", columnDefinition = "NVARCHAR(255)")
    private String saturatedFat;

    @Builder
    public Nutrition(String calorie, String carbohydrate, String sugars, String salt, String protein, String province, String cholesterol, String transFat, String caffeine, String saturatedFat) {
        this.calorie = calorie;
        this.carbohydrate = carbohydrate;
        this.sugars = sugars;
        this.salt = salt;
        this.protein = protein;
        this.province = province;
        this.cholesterol = cholesterol;
        this.transFat = transFat;
        this.caffeine = caffeine;
        this.saturatedFat = saturatedFat;
    }

    @PrePersist
    public void prePersist() {
        this.calorie = this.calorie == null ? "0kcal" : this.calorie;
        this.carbohydrate = this.carbohydrate == null ? "0g" : this.carbohydrate;
        this.sugars = this.sugars == null ? "0g" : this.sugars;
        this.salt = this.salt == null ? "0mg" : this.salt;
        this.protein = this.protein == null ? "0g" : this.protein;
        this.province = this.province == null ? "0g" : this.province;
        this.cholesterol = this.cholesterol == null ? "0mg" : this.cholesterol;
        this.transFat = this.transFat == null ? "0g" : this.transFat;
        this.caffeine = this.caffeine == null ? "0mg" : this.caffeine;
        this.saturatedFat = this.saturatedFat == null ? "0g" : this.saturatedFat;
    }

    public void update(NutritionCreateRequest nutrition) {
        setCalorie(nutrition.addUnit(nutrition.getCalorie(), NutritionCreateRequest.Unit.KCAL));
        setCarbohydrate(nutrition.addUnit(nutrition.getCarbohydrate(), NutritionCreateRequest.Unit.GRAM));
        setSugars(nutrition.addUnit(nutrition.getSugars(), NutritionCreateRequest.Unit.GRAM));
        setSalt(nutrition.addUnit(nutrition.getSalt(), NutritionCreateRequest.Unit.MILLI_GRAM));
        setProtein(nutrition.addUnit(nutrition.getProtein(), NutritionCreateRequest.Unit.GRAM));
        setProvince(nutrition.addUnit(nutrition.getProvince(), NutritionCreateRequest.Unit.GRAM));
        setCholesterol(nutrition.addUnit(nutrition.getCholesterol(), NutritionCreateRequest.Unit.MILLI_GRAM));
        setTransFat(nutrition.addUnit(nutrition.getTransFat(), NutritionCreateRequest.Unit.GRAM));
        setCaffeine(nutrition.addUnit(nutrition.getCaffeine(), NutritionCreateRequest.Unit.MILLI_GRAM));
        setSaturatedFat(nutrition.addUnit(nutrition.getSaturatedFat(), NutritionCreateRequest.Unit.GRAM));
    }

}
