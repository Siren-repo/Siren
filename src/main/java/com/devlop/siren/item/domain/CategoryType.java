package com.devlop.siren.item.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum CategoryType {
    BEVERAGE("음료"),
    FOOD("푸드"),
    PRODUCT("상품");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static CategoryType of(String typeName) {
        return Arrays.stream(values())
                .filter(categoryType -> Objects.equals(categoryType.name(), typeName))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 타입 입니다."));
    }
}
