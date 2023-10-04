package com.devlop.siren.unit.service;

import com.devlop.siren.domain.user.domain.AllergyType;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.domain.user.util.AllergyConverter;
import com.devlop.siren.global.exception.GlobalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private AllergyConverter converter;
    @InjectMocks
    private UserService service;

    @Test
    @DisplayName("요청한 내용으로 회원가입을 진행한다")
    void register(){
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@test.com")
                .password("password")
                .allergies("allergies")
                .build();
        EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT);

        when(encoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(converter.convertToEntityAttribute(request.getAllergies())).thenReturn(allergies);

        service.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이메일이 중복되어 요청한 정보로 회원가입을 할 수 없다")
    void registerWithDuplicatedUser(){
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@test.com")
                .password("password")
                .allergies("allergies")
                .build();
        EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT);
        User duplicatedUser = UserRegisterRequest.fromDto(request, "encodedPassword", UserRole.CUSTOMER, allergies);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(duplicatedUser));
        GlobalException e = Assertions.assertThrows(GlobalException.class, () -> service.register(request));

        Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.CONFLICT);
    }
}
