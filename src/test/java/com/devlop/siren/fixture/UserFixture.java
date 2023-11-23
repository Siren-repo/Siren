package com.devlop.siren.fixture;

import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import java.util.EnumSet;

public class UserFixture {
  public static User get(String email, String password, String nickName) {
    return User.builder()
        .email(email)
        .password(password)
        .nickName(nickName)
        .role(UserRole.CUSTOMER)
        .phone("010-0000-0000")
        .allergies(EnumSet.of(AllergyType.PEANUT, AllergyType.MILK))
        .build();
  }

  public static UserRegisterRequest get(String email, String password) {
    return UserRegisterRequest.builder()
        .email(email)
        .password(password)
        .nickName("닉네임")
        .phone("010-0000-0000")
        .allergies("PEANUT, MILK")
        .build();
  }
}
