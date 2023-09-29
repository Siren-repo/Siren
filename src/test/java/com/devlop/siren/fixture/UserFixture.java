package com.devlop.siren.fixture;

import com.devlop.siren.unit.domain.user.domain.AllergyType;
import com.devlop.siren.unit.domain.user.domain.User;
import com.devlop.siren.unit.domain.user.domain.UserRole;

import java.util.EnumSet;

public class UserFixture {
    public static User get(String email, String password, UserRole role, EnumSet<AllergyType> allergies){
        return User.builder()
                .email(email)
                .password(password)
                .role(role)
                .allergies(allergies)
                .build();
    }
}
