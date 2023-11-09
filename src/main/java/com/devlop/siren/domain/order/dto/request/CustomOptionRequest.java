package com.devlop.siren.domain.order.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Getter
public class CustomOptionRequest {
    private String cupSize;
    private String espressoType;
    private Integer espressoShotCnt;
    private String milkType;
    private String milkAmount;
    private String foamType;
    private String foamAmount;
    private Set<SyrupDto> syrups;
    private Set<DrizzleDto> drizzles;

    private Set<PotionDto> potions;

    @Builder
    public CustomOptionRequest(String cupSize, String espressoType, Integer espressoShotCnt,
                               String milkType, String milkAmount, String foamType, String foamAmount,
                               Set<SyrupDto> syrups, Set<DrizzleDto> drizzles, Set<PotionDto> potions) {
        this.cupSize = cupSize;
        this.espressoType = espressoType;
        this.espressoShotCnt = espressoShotCnt;
        this.milkType = milkType;
        this.milkAmount = milkAmount;
        this.foamType = foamType;
        this.foamAmount = foamAmount;
        this.syrups = syrups;
        this.drizzles = drizzles;
        this.potions = potions;
    }
    @Getter
    @NoArgsConstructor
    public static class SyrupDto{
        String syrupType;
        int cnt;
    }
    @Getter
    @NoArgsConstructor
    public static class DrizzleDto{
        String drizzleType;
        int cnt;
    }
    @Getter
    @NoArgsConstructor
    public static class PotionDto{
        String potionType;
        int cnt;
    }

}
