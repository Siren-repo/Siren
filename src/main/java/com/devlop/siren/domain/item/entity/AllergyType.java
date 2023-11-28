package com.devlop.siren.domain.item.entity;

import lombok.Getter;

@Getter
public enum AllergyType {
  MILK("우유"),
  SOYBEAN("대두"),
  WHEAT("밀"),
  PEANUT("땅콩"),
  TOMATO("토마토"),
  PEACH("복숭아"),
  SQUID("오징어");

  private final String allergyName;

  AllergyType(String allergyName) {
    this.allergyName = allergyName;
  }
}
