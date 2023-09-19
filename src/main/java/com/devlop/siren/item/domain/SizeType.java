package com.devlop.siren.item.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

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
                .filter(sizeType -> Objects.equals(sizeType.englishName, size))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
    }
}
