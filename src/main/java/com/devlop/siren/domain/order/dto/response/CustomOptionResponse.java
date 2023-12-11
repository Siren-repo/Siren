package com.devlop.siren.domain.order.dto.response;

import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.entity.option.OptionDetails.DrizzleDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.EspressoDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.FoamDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.SyrupDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.MilkType;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.Temperature;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.order.domain.option.BeverageOption;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import com.devlop.siren.domain.order.domain.option.FoodOption;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
public class CustomOptionResponse {
  private Boolean takeout;
  private Temperature temperature;
  private int amount;
  private SizeType cupSize;
  private EspressoDetail espresso;
  private MilkType milk;
  private FoamDetail foam;
  private Set<SyrupDetail> syrup;
  private Set<DrizzleDetail> drizzle;
  private Set<PotionDetail> potion;

  @Builder
  public CustomOptionResponse(
      Boolean takeout,
      Temperature temperature,
      int amount,
      SizeType cupSize,
      EspressoDetail espresso,
      MilkType milk,
      FoamDetail foam,
      Set<SyrupDetail> syrup,
      Set<DrizzleDetail> drizzle,
      Set<PotionDetail> potion) {
    this.takeout = takeout;
    this.temperature = temperature;
    this.amount = amount;
    this.cupSize = cupSize;
    this.espresso = espresso;
    this.milk = milk;
    this.foam = foam;
    this.syrup = syrup;
    this.drizzle = drizzle;
    this.potion = potion;
  }

  public static Optional<CustomOptionResponse> fromEntity(
      CustomOption option, CategoryType category) {
    switch (category.name()) {
      case "BEVERAGE":
        return Optional.of(mapBeverageOption((BeverageOption) option));
      case "FOOD":
        return Optional.of(mapFoodOption((FoodOption) option));
      default:
        return Optional.empty();
    }
  }

  private static CustomOptionResponse mapBeverageOption(BeverageOption option) {
    return CustomOptionResponse.builder()
        .takeout(option.getTakeout())
        .temperature(option.getTemperature())
        .amount(option.getAdditionalAmount())
        .cupSize(option.getCupSize())
        .espresso(option.getEspresso())
        .foam(option.getFoam())
        .milk(option.getMilk())
        .syrup(option.getSyrup())
        .drizzle(option.getDrizzle())
        .build();
  }

  private static CustomOptionResponse mapFoodOption(FoodOption option) {
    return CustomOptionResponse.builder()
        .takeout(option.getTakeout())
        .temperature(option.getTemperature())
        .amount(option.getAdditionalAmount())
        .potion(option.getPotion())
        .build();
  }
}
