package com.devlop.siren.domain.order.domain.option;

import lombok.Getter;

@Getter
public enum PriceType {
    SIZE_UP(500),
    CHANGE_OAT_MILK(600),

    ADD_ESPRESSO_SHOT(600),
    ADD_SYRUP(600),
    ADD_DRIZZLE(600),
    ADD_WHIPPED_CREAM(600)
    ;

    private int price;

    PriceType(int additionalPrice){
        this.price = additionalPrice;
    }

}
