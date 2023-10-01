package com.devlop.siren.domain.category.entity;

import com.devlop.siren.global.exception.InvalidCategoryTypeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

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
                .filter(categoryType -> categoryType.name.equals(typeName) || categoryType.name().equals(typeName))
                .findFirst().orElseThrow(() -> new InvalidCategoryTypeException());
    }

    @Override
    public String toString() {
        return "CategoryType{" +
                "name='" + name + '\'' +
                '}';
    }
}
