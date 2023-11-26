package com.devlop.siren.domain.order.dto.request;

import com.devlop.siren.domain.item.entity.option.OptionDetails.DrizzleDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.EspressoDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.FoamDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.SyrupDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.MilkType;
import com.devlop.siren.domain.item.entity.option.SizeType;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomOptionRequest {
  private SizeType cupSize;
  private Optional<EspressoDetail> espresso;
  private Optional<MilkType> milk;
  private Optional<FoamDetail> foam;
  private Optional<Set<SyrupDetail>> syrups;
  private Optional<Set<DrizzleDetail>> drizzles;
  private Optional<Set<PotionDetail>> potions;

  @Builder
  public CustomOptionRequest(
      SizeType cupSize,
      EspressoDetail espresso,
      MilkType milk,
      FoamDetail foam,
      Set<SyrupDetail> syrups,
      Set<DrizzleDetail> drizzles,
      Set<PotionDetail> potions) {
    this.cupSize = cupSize;
    this.espresso = Optional.ofNullable(espresso);
    this.milk = Optional.ofNullable(milk);
    this.foam = Optional.ofNullable(foam);
    this.syrups = Optional.ofNullable(syrups);
    this.drizzles = Optional.ofNullable(drizzles);
    this.potions = Optional.ofNullable(potions);
  }
}
