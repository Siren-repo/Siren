package com.devlop.siren.item.domain;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Allergy {
    MILK("우유"),
    SOYBEAN("대두"),
    WHEAT("밀"),
    PEANUT("땅콩"),
    TOMATO("토마토"),
    PEACH("복숭아"),
    SQUID("오징어");

    private final String allergyName;

    Allergy(String allergyName) {
        this.allergyName = allergyName;
    }


    public static EnumSet<Allergy> convertToEnumSet(String allergy) {
        if (Strings.isEmpty(allergy)) {
            return EnumSet.noneOf(Allergy.class);
        }
        return (EnumSet<Allergy>) Arrays.stream(allergy.split(","))
                .map(name -> Allergy.valueOf(allergy))
                .collect(Collectors.toUnmodifiableList());
    }

    public static String convertToString(EnumSet<Allergy> allergies){
        if (allergies.isEmpty() || allergies == null) {
            return "";
        }
        List<String> allergy = allergies.stream()
                .map(Enum::name)
                .collect(Collectors.toUnmodifiableList());
        return String.join(",", allergy);
    }
}
