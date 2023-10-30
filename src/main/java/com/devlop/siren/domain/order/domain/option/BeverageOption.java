package com.devlop.siren.domain.order.domain.option;

import com.devlop.siren.domain.item.entity.SizeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("Beverage")
@Table(name = "beverage_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BeverageOption extends CustomOption {
    @Column(name = "cup_size", nullable = false)
    @Enumerated(EnumType.STRING)
    private SizeType cupSize = SizeType.TALL;
    @Column(name = "espresso_type")
    @Enumerated(EnumType.STRING)
    private EspressoType espressoType = EspressoType.ORIGINAL;
    @Column(name = "espresso_shot_cnt")
    private Integer espressoShotCnt;
    @Column(name = "milk_type")
    @Enumerated(EnumType.STRING)
    private MilkType milkType = MilkType.ORIGINAL;
    @Column(name = "whipped_cream_amount")
    @Enumerated(EnumType.STRING)
    private WhippedCreamAmount whippedCreamAmount;

    @ElementCollection
    @CollectionTable(name = "syrup_details",
            joinColumns = @JoinColumn(name="custom_option_id"))
    @Column(name = "syrup_type")
    private Set<SyrupDetail> syrupTypes = new HashSet<SyrupDetail>();

    @ElementCollection
    @CollectionTable(name = "drizzle_details",
            joinColumns = @JoinColumn(name="custom_option_id"))
    @Column(name = "drizzle_type")
    private Set<DrizzleDetail> drizzleTypes = new HashSet<DrizzleDetail>();

    @Override
    public int getPrice() {
        int additionalPrice = 0;

        if(!this.cupSize.equals(SizeType.TALL)){
            additionalPrice += PriceType.SIZE_UP.getPrice() * cupSize.ordinal();
        }
        if(this.milkType.equals(MilkType.OAT))
            additionalPrice += PriceType.CHANGE_OAT_MILK.getPrice();

        if(!this.espressoType.equals(EspressoType.ORIGINAL))
            additionalPrice += PriceType.ADD_ESPRESSO_SHOT.getPrice() * this.espressoShotCnt;

        if(this.whippedCreamAmount != null)
            additionalPrice += PriceType.ADD_WHIPPED_CREAM.getPrice();

        if(this.syrupTypes.size() > 0){
            additionalPrice += syrupTypes.stream()
                    .map(syrup -> PriceType.ADD_SYRUP.getPrice() * syrup.getCnt())
                    .mapToInt(Integer::intValue)
                    .sum();
        }
        if(this.drizzleTypes.size() > 0){
            additionalPrice += drizzleTypes.stream()
                    .map(drizzle -> PriceType.ADD_DRIZZLE.getPrice() * drizzle.getCnt())
                    .mapToInt(Integer::intValue)
                    .sum();
        }
        return additionalPrice;
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    public class SyrupDetail {
        @Enumerated(EnumType.STRING)
        private SyrupType type;
        private int cnt;
        public SyrupDetail(SyrupType type, int cnt){
            this.type = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public class DrizzleDetail {
        @Enumerated(EnumType.STRING)
        DrizzleType type;
        int cnt;
        public DrizzleDetail(DrizzleType type, int cnt){
            this.type = type;
            this.cnt = cnt;
        }
    }
    enum EspressoType{
        ORIGINAL, DECAFFEINE, BLOND, HALF_DECAFFEINE
    }
    enum MilkType{
        ORIGINAL, LOW_FAT, FAT_FREE, SOY, OAT
    }
    enum WhippedCreamAmount{
        LITTLE, MUCH, NORMAL, NONE;
    }
    enum SyrupType{
        VANILLA, CARAMEL, HAZELNUT, CLASSIC;
    }
    enum DrizzleType{
        CHOCOLATE, CARAMEL;
    }
}

