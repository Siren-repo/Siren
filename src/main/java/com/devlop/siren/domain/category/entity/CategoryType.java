package com.devlop.siren.domain.category.entity;

import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.Getter;

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
                .findFirst().orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.INVALID_CATEGORY_TYPE));
    }

    @Override
    public String toString() {
        return "CategoryType{" +
                "name='" + name + '\'' +
                '}';
    }
}
