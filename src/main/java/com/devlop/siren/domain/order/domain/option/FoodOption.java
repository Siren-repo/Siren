package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.Temperature;
import com.devlop.siren.domain.order.dto.request.CustomOptionRequest;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Food")
@NoArgsConstructor
@Table(name = "food_options")
public class FoodOption extends CustomOption {
  @ElementCollection
  @CollectionTable(name = "potion_details", joinColumns = @JoinColumn(name = "custom_option_id"))
  @Column(name = "potion_type")
  private Set<PotionDetail> potion = new HashSet<PotionDetail>();

  @Builder
  public FoodOption(Item item, Boolean isWarmed, Boolean isTakekout, Set<PotionDetail> potions) {
    takeout = isTakekout;

    String categoryName = item.getCategory().getCategoryName();
    if (categoriesRequiringWarming.contains(categoryName)) {
      temperature = isWarmed ? Temperature.HOT : Temperature.NONE;
    }

    if (categoriesWithDefaultOptions.contains(categoryName)) {
      temperature = Temperature.NONE;
    }

    if (categoriesWithPotionOption.contains(categoryName)) {
      potion = potions;
    }

    amount = getAdditionalAmount();
  }

  public static FoodOption fromDto(CustomOptionRequest request, Boolean takeout, Boolean warm) {
    return FoodOption.builder()
        .isWarmed(warm)
        .isTakekout(takeout)
        .potions(request.getPotions().orElse(new HashSet<>()))
        .build();
  }

  @Override
  public Integer getAdditionalAmount() {
    int additionalAmount = 0;
    if (potion.size() > 0) {
      additionalAmount +=
          potion.stream()
              .mapToInt(potion -> potion.getPotionType().getPrice() * potion.getCnt())
              .sum();
    }
    return additionalAmount;
  }
}
