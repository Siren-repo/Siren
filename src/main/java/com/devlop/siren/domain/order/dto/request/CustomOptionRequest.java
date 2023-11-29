package com.devlop.siren.domain.order.dto.request;

import com.devlop.siren.domain.item.entity.option.OptionDetails.DrizzleDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.EspressoDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.FoamDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.SyrupDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.MilkType;
import com.devlop.siren.domain.item.entity.option.SizeType;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CustomOptionRequest {
  private SizeType cupSize;
  private EspressoDetail espresso;
  private MilkType milk;
  private FoamDetail foam;
  private Set<SyrupDetail> syrups;
  private Set<DrizzleDetail> drizzles;
  private Set<PotionDetail> potions;

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
    this.espresso = Objects.requireNonNullElse(espresso, null);
    this.milk = Objects.requireNonNullElse(milk, null);
    this.foam = Objects.requireNonNullElse(foam, null);
    this.syrups = Objects.requireNonNullElse(syrups, null);
    this.drizzles = Objects.requireNonNullElse(drizzles, null);
    this.potions = Objects.requireNonNullElse(potions, null);
  }
}
