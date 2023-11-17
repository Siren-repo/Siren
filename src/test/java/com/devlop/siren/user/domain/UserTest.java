package com.devlop.siren.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.fixture.UserFixture;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void changeUserRole() {
        User entity = UserFixture.get("test@test.com", "password", "닉네임");

        entity.changeRole(UserRole.STAFF);

        assertEquals(entity.getRole(), UserRole.STAFF);
    }

    @Test
    void validatePasswordLength() {
        //given
        User entity = UserFixture.get("test@test.com", "wrong", "닉네임");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        //when
        Set<ConstraintViolation<User>> violations = validator.validate(entity);
        ConstraintViolation<User> violation = violations.iterator().next();

        //then
        assertEquals("비밀번호는 8자 이상이어야 합니다", violation.getMessage()); // 예상한 메세지와 비교합니다.
    }

    @Test
    void validateNickName() {
        //given
        User entity = UserFixture.get("test@test.com", "password", "harper");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        //when
        Set<ConstraintViolation<User>> violations = validator.validate(entity);
        ConstraintViolation<User> violation = violations.iterator().next();

        //then
        assertEquals("닉네임은 한글로만 설정할 수 있습니다", violation.getMessage()); // 예상한 메세지와 비교합니다.
    }
}
