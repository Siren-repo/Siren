package com.devlop.siren.domain.order.dto.request;

import com.devlop.siren.domain.item.entity.option.OptionDetails.DrizzleDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.EspressoDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.FoamDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.SyrupDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.MilkType;
import com.devlop.siren.domain.item.entity.option.SizeType;
import java.util.Set;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
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
    this.espresso = espresso;
    this.milk = milk;
    this.foam = foam;
    this.syrups = syrups;
    this.drizzles = drizzles;
    this.potions = potions;
  }
}
