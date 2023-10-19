package com.devlop.siren.domain.item.dto.response;

import com.devlop.siren.domain.item.entity.DefaultOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultOptionResponse {
    private Integer espressoShotCount;

    private Integer vanillaSyrupCount;

    private Integer caramelSyrupCount;

    private Integer hazelnutSyrupCount;

    private String size;

    public static DefaultOptionResponse from(DefaultOption defaultOption) {
        return new DefaultOptionResponse(
                defaultOption.getEspressoShotCount(),
                defaultOption.getVanillaSyrupCount(),
                defaultOption.getCaramelSyrupCount(),
                defaultOption.getHazelnutSyrupCount(),
                defaultOption.getSize().getEnglishName());
    }
}
