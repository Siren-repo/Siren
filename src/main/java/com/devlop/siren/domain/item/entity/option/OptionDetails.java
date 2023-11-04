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
        private SyrupType type;

        private Integer cnt;
        public SyrupDetail(SyrupType type, int cnt){
            this.type = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class DrizzleDetail {
        @Enumerated(EnumType.STRING)
        private DrizzleType type;

        private Integer cnt;

        public DrizzleDetail(DrizzleType type, int cnt){
            this.type = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class EspressoDetail{
        @Enumerated(EnumType.STRING)
        private EspressoType type;

        private Integer cnt;

        public EspressoDetail(EspressoType type, int cnt){
            this.type = type;
            this.cnt = cnt;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class MilkDetail{
        @Enumerated(EnumType.STRING)
        private MilkType type;

        @Enumerated(EnumType.STRING)
        private Amount amount;

        public MilkDetail(MilkType type, Amount amount){
            this.type = type;
            this.amount = amount;
        }
    }
    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class FoamDetail{
        @Enumerated(EnumType.STRING)
        private FoamType type;

        @Enumerated(EnumType.STRING)
        private Amount amount;

        public FoamDetail(FoamType type, Amount amount){
            this.type = type;
            this.amount = amount;
        }
    }

    @Embeddable
    @Getter
    @NoArgsConstructor
    public static class PotionDetail{
        @Enumerated(EnumType.STRING)
        private PotionType type;
        private Integer cnt;

        public PotionDetail(PotionType type, int cnt){
            this.type = type;
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
