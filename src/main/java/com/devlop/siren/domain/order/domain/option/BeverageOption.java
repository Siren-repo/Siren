package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.option.OptionDetails.DrizzleDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.EspressoDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.FoamDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.SyrupDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.MilkType;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.order.dto.request.CustomOptionRequest;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Beverage")
@Table(name = "beverage_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BeverageOption extends CustomOption {
  @Column(name = "cup_size", nullable = false)
  @Enumerated(EnumType.STRING)
  private SizeType cupSize = SizeType.TALL;

  @Column(name = "espresso_type")
  @Enumerated(EnumType.STRING)
  private EspressoDetail espresso;

  @Column(name = "milk_type")
  @Enumerated(EnumType.STRING)
  private MilkType milk;

  @Column(name = "foam_type")
  @Enumerated(EnumType.STRING)
  private FoamDetail foam;

  @ElementCollection
  @CollectionTable(name = "syrup_details", joinColumns = @JoinColumn(name = "custom_option_id"))
  @Column(name = "syrup_type")
  private Set<SyrupDetail> syrup;

  @ElementCollection
  @CollectionTable(name = "drizzle_details", joinColumns = @JoinColumn(name = "custom_option_id"))
  @Column(name = "drizzle_type")
  private Set<DrizzleDetail> drizzle;

  @Builder
  public BeverageOption(
      Boolean isTakeout,
      Boolean isWarmed,
      SizeType cupSize,
      EspressoDetail espresso,
      MilkType milk,
      FoamDetail foam,
      Set<SyrupDetail> syrup,
      Set<DrizzleDetail> drizzle) {
    setTakeout(isTakeout);
    setTemperature(isWarmed);
    this.cupSize = cupSize;
    this.espresso = espresso;
    this.milk = milk;
    this.foam = foam;
    this.syrup = syrup;
    this.drizzle = drizzle;
    this.amount = getAdditionalAmount();
  }

  public static BeverageOption fromDto(CustomOptionRequest request, Boolean takeout, Boolean warm) {
    return BeverageOption.builder()
        .isTakeout(takeout)
        .isWarmed(warm)
        .cupSize(request.getCupSize())
        .espresso(request.getEspresso())
        .milk(request.getMilk())
        .foam(request.getFoam())
        .syrup(Objects.requireNonNullElse(request.getSyrups(), new HashSet<>()))
        .drizzle(Objects.requireNonNullElse(request.getDrizzles(), new HashSet<>()))
        .build();
  }

  @Override
  public Integer getAdditionalAmount() {
    int additionalAmount = 0;

    if (!this.cupSize.equals(SizeType.TALL)) {
      additionalAmount += PriceType.SIZE_UP.getPrice() * this.cupSize.ordinal();
    }

    if (this.espresso != null) {
      additionalAmount += PriceType.ADD_ESPRESSO_SHOT.getPrice() * this.espresso.getCnt();
    }

    if (this.milk != null && this.milk.equals(MilkType.OAT)) {
      additionalAmount += PriceType.CHANGE_OAT_MILK.getPrice();
    }

    if (this.foam != null) {
      additionalAmount += PriceType.ADD_FOAM.getPrice();
    }

    if (this.syrup.size() > 0) {
      additionalAmount +=
          syrup.stream().mapToInt(syrup -> PriceType.ADD_SYRUP.getPrice() * syrup.getCnt()).sum();
    }

    if (this.drizzle.size() > 0) {
      additionalAmount +=
          drizzle.stream()
              .mapToInt(drizzle -> PriceType.ADD_DRIZZLE.getPrice() * drizzle.getCnt())
              .sum();
    }

    return additionalAmount;
  }
}
