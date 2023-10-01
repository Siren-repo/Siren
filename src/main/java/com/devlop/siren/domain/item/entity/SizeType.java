package com.devlop.siren.domain.item.entity;

import com.devlop.siren.global.exception.InvalidSizeTypeException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SizeType {
    TALL("Tall", "355ml"),
    GRANDE("Grande", "473ml"),
    VENTI("Venti", "591ml"),
    TRENTA("Trenta", "887ml");

    private final String englishName;
    private final String amount;

    SizeType(String englishName, String amount) {
        this.amount = amount;
        this.englishName = englishName;
    }

    @JsonCreator
    public static SizeType of(String size) {
        return Arrays.stream(values())
                .filter(sizeType -> sizeType.englishName.equals(size) || sizeType.name().equals(size))
                .findFirst().orElseThrow(() -> new InvalidSizeTypeException());
    }
}
