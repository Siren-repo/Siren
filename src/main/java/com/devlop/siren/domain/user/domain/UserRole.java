package com.devlop.siren.domain.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  ADMIN(101, "관리자"),
  STAFF(102, "스태프"),
  CUSTOMER(103, "고객");

  private final int code;
  private final String desc;
}
