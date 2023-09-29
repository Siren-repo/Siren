package com.devlop.siren.unit.domain;

import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.unit.domain.user.domain.AllergyType;
import com.devlop.siren.unit.domain.user.domain.User;
import com.devlop.siren.unit.domain.user.domain.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

public class UserTest {
    @Test
    void changeUserRole(){
        User entity = UserFixture.get("test@test.com", "password", UserRole.CUSTOMER, EnumSet.of(AllergyType.PEANUT));

        entity.changeRole(UserRole.STAFF);

        Assertions.assertEquals(entity.getRole(), UserRole.STAFF);
    }
}
