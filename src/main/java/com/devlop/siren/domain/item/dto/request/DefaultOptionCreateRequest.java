package com.devlop.siren.domain.item.dto.request;

import com.devlop.siren.domain.item.entity.option.DefaultOption;
import com.devlop.siren.domain.item.entity.option.SizeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultOptionCreateRequest {

    private Integer espressoShotCount;

    private Integer vanillaSyrupCount;

    private Integer caramelSyrupCount;

    private Integer hazelnutSyrupCount;

    private SizeType size;


    public static DefaultOption toEntity(DefaultOptionCreateRequest defaultOptionCreateRequest) {
        return DefaultOption.builder()
                .espressoShotCount(defaultOptionCreateRequest.espressoShotCount)
                .vanillaSyrupCount(defaultOptionCreateRequest.vanillaSyrupCount)
                .caramelSyrupCount(defaultOptionCreateRequest.caramelSyrupCount)
                .size(defaultOptionCreateRequest.size)
                .hazelnutSyrupCount(defaultOptionCreateRequest.hazelnutSyrupCount).build();
    }

}