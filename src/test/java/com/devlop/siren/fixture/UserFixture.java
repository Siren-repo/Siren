package com.devlop.siren.fixture;

import com.devlop.siren.domain.user.domain.AllergyType;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;

import java.util.EnumSet;

public class UserFixture {
    public static User get(String email, String password, String nickName, UserRole role, EnumSet<AllergyType> allergies){
        return User.builder()
                .email(email)
                .password(password)
                .nickName(nickName)
                .role(role)
                .allergies(allergies)
                .build();
    }
}
