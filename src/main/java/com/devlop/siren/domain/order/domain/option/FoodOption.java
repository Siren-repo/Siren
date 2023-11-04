package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.option.OptionDetails.*;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.*;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Food")
@NoArgsConstructor
@Table(name = "food_options")
public class FoodOption extends CustomOption{
    /*
    * 추가 옵션 없는 카테고리: 케이크, 샐러드, 아이스크림, 과일, 요거트, 스낵
    * 포션 옵션 추가되는 카테고리: 브레드
    * 데움 옵션 추가되는 카테고리: 샌드위치, 브레드, 수프
    * */
    @ElementCollection
    @CollectionTable(name = "potion_details",
            joinColumns = @JoinColumn(name="custom_option_id"))
    @Column(name = "potion_type")
    private Set<PotionDetail> potion = new HashSet<PotionDetail>();

    static Set<String> categoriesWithoutOptions = Set.of("Cake", "Salad", "IceCream", "Fruit", "Yogurt", "Snack");
    static Set<String> categoriesWithPotionOption = Set.of("Bread");
    static Set<String> categoriesRequiringWarming = Set.of("Sandwich", "Bread", "Soup");

    @Builder
    public FoodOption(Item item, Boolean isWarmed, Boolean isTakeOut, Set<PotionDetail> potions) {
        takeout = isTakeOut;

        String categoryName = item.getCategory().getCategoryName();
        if(categoriesRequiringWarming.contains(categoryName))
            temperature = isWarmed ? Temperature.HOT : Temperature.NONE;

        if(categoriesWithoutOptions.contains(categoryName)){
            temperature = Temperature.NONE;
        }

        if(categoriesWithPotionOption.contains(categoryName)){
            potion = potions;
        }
    }
    @Override
    public int getAdditionalAmount() {
        if(potion.size() > 0){
            amount += potion.stream()
                    .mapToInt(potion -> potion.getType().getPrice() * potion.getCnt())
                    .sum();
        }
        return amount;
    }


}
