package com.devlop.siren.domain.item.entity.option;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.*;

public class OptionDetails {
    @Embeddable
    @Getter
    @NoArgsConstructor
    public class SyrupDetail {
        @Enumerated(EnumType.STRING)
        private SyrupType syrupType;

        private Integer cnt;
        public SyrupDetail(SyrupType type, int cnt){
            this.syrupType = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class DrizzleDetail {
        @Enumerated(EnumType.STRING)
        private DrizzleType drizzleType;

        private Integer cnt;

        public DrizzleDetail(DrizzleType type, int cnt){
            this.drizzleType = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class EspressoDetail{
        @Enumerated(EnumType.STRING)
        private EspressoType espressoType;

        private Integer cnt;

        public EspressoDetail(EspressoType type, int cnt){
            this.espressoType = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class MilkDetail{
        @Enumerated(EnumType.STRING)
        private MilkType milkType;

        @Enumerated(EnumType.STRING)
        private Amount milkAmount;

        public MilkDetail(MilkType type, Amount amount){
            this.milkType = type;
            this.milkAmount = amount;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class FoamDetail{
        @Enumerated(EnumType.STRING)
        private FoamType foamType;

        @Enumerated(EnumType.STRING)
        private Amount foamAmount;

        public FoamDetail(FoamType type, Amount amount){
            this.foamType = type;
            this.foamAmount = amount;
        }
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class PotionDetail{
        @Enumerated(EnumType.STRING)
        private PotionType potionType;
        private Integer cnt;

        public PotionDetail(PotionType type, int cnt){
            this.potionType = type;
            this.cnt = cnt;
        }
    }
    @Getter
    @NoArgsConstructor
    public enum PotionType{
        BUTTER(500),
        CREAM_CHEESE(1000);

        private Integer price;
        PotionType(Integer price) {
            this.price = price;
        }
    }
}
