package com.devlop.siren.unit.repository;

import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.unit.domain.user.domain.AllergyType;
import com.devlop.siren.unit.domain.user.domain.User;
import com.devlop.siren.unit.domain.user.domain.UserRole;
import com.devlop.siren.unit.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.EnumSet;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown(){
        userRepository.deleteAllInBatch();
    }

    @Test
    void saveUser(){
        //given
        EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);
        User entity = UserFixture.get("test@test.com", "password", "닉네임", UserRole.CUSTOMER, allergies);

        //when
        User savedUser = userRepository.save(entity);

        //then
        Assertions.assertEquals(savedUser.getAllergies(), allergies);
        Assertions.assertEquals(savedUser.getEmail(), "test@test.com");
    }

    @Test
    void findByEmail(){
        //given
        EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);
        String requestEmail = "test@test.com";
        User entity = UserFixture.get(requestEmail, "password", "닉네임", UserRole.CUSTOMER, allergies);

        //when
        userRepository.save(entity);
        User savedUser = userRepository.findByEmail(requestEmail).get();

        //then
        Assertions.assertEquals(savedUser.getEmail(), requestEmail);
    }

}