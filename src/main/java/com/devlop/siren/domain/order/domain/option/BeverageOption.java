package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.option.OptionDetails.*;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private MilkDetail milk;

    @Column(name = "foam_type")
    @Enumerated(EnumType.STRING)
    private FoamDetail foam;

    @ElementCollection
    @CollectionTable(name = "syrup_details",
            joinColumns = @JoinColumn(name="custom_option_id"))
    @Column(name = "syrup_type")
    private Set<SyrupDetail> syrup = new HashSet<SyrupDetail>();

    @ElementCollection
    @CollectionTable(name = "drizzle_details",
            joinColumns = @JoinColumn(name="custom_option_id"))
    @Column(name = "drizzle_type")
    private Set<DrizzleDetail> drizzle = new HashSet<DrizzleDetail>();

    @Builder
    public BeverageOption(Boolean takeout, SizeType cupSize, EspressoDetail espresso, MilkDetail milk,
                          FoamDetail foam, Set<SyrupDetail> syrup, Set<DrizzleDetail> drizzle) {
        this.takeout = takeout;
        this.cupSize = cupSize;
        this.espresso = espresso;
        this.milk = milk;
        this.foam = foam;
        this.syrup = syrup;
        this.drizzle = drizzle;
    }

    @Override
    public int getAdditionalAmount() {
        if(!cupSize.equals(SizeType.TALL))
            amount += PriceType.SIZE_UP.getPrice() * cupSize.ordinal();

        if(espresso != null)
            amount += PriceType.ADD_ESPRESSO_SHOT.getPrice() * espresso.getCnt();

        if(milk != null && milk.getType().equals(MilkType.OAT))
            amount += PriceType.CHANGE_OAT_MILK.getPrice();

        if(foam != null)
            amount += PriceType.ADD_FOAM.getPrice();

        if(syrup.size() > 0){
            amount += syrup.stream()
                    .mapToInt(syrup -> PriceType.ADD_SYRUP.getPrice() * syrup.getCnt())
                    .sum();
        }

        if(drizzle.size() > 0){
            amount += drizzle.stream()
                    .mapToInt(drizzle -> PriceType.ADD_DRIZZLE.getPrice() * drizzle.getCnt())
                    .sum();
        }

        return amount;
    }
}

