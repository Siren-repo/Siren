package com.devlop.siren.user.repository;

import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.fixture.UserFixture;
import java.util.EnumSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired private UserRepository userRepository;

  @AfterEach
  void tearDown() {
    userRepository.deleteAllInBatch();
  }

  @Test
  void saveUser() {
    // given
    EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);
    String requestEmail = "test@test.com";
    User entity = UserFixture.get(requestEmail, "password", "닉네임");

    // when
    User savedUser = userRepository.save(entity);

    // then
    Assertions.assertEquals(savedUser.getAllergies(), allergies);
    Assertions.assertEquals(savedUser.getEmail(), requestEmail);
  }

  @Test
  void findByEmail() {
    // given
    String requestEmail = "test@test.com";
    User entity = UserFixture.get(requestEmail, "password", "닉네임");

    // when
    userRepository.save(entity);
    User savedUser = userRepository.findByEmail(requestEmail).get();

    // then
    Assertions.assertEquals(savedUser.getEmail(), requestEmail);
  }
}
