package com.devlop.siren.domain.user.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AllergyType {
    MILK("우유"),
    SOYBEAN("대두"),
    WHEAT("밀"),
    PEANUT("땅콩"),
    TOMATO("토마토"),
    PEACH("복숭아"),
    SQUID("오징어");

    private final String allergyName;
}