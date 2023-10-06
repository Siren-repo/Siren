package com.devlop.siren.unit.service;

import com.devlop.siren.domain.user.domain.AllergyType;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.domain.user.service.RedisService;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.domain.user.util.AllergyConverter;
import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.global.exception.GlobalException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;


import java.time.Duration;
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
    @Mock
    private RedisService redisService;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(userService, "secretKey", "2023-wanted-internship-siren-order-project.secret_key");
        ReflectionTestUtils.setField(userService, "accessExpiredTimeMs", 43200000L);
        ReflectionTestUtils.setField(userService, "refreshExpiredTimeMs", 604800000L);
    }

    @Test
    @DisplayName("요청한 내용으로 회원가입을 진행한다")
    void register(){

        UserRegisterRequest request = UserFixture.get("email", "encodedPassword");
        EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);

        when(encoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(converter.convertToEntityAttribute(request.getAllergies())).thenReturn(allergies);

        userService.register(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이메일이 중복되어 요청한 정보로 회원가입을 할 수 없다")
    void registerWithDuplicatedUser(){
        UserRegisterRequest request = UserFixture.get("email", "password");
        EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);
        User duplicatedUser = UserRegisterRequest.fromDto(request, "encodedPassword", UserRole.CUSTOMER, allergies);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(duplicatedUser));
        GlobalException e = Assertions.assertThrows(GlobalException.class, () -> userService.register(request));

        Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("요청한 이메일과 패스워드로 로그인을 진행한다")
    void login(){
        UserLoginRequest request = new UserLoginRequest("test@test.com", "encodedPassword");
        User registedUser = UserFixture.get("test@test.com", "encodedPassword", "닉네임");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(registedUser));
        when(encoder.matches(request.getPassword(), registedUser.getPassword())).thenReturn(true);
        doNothing().when(redisService).setValue(anyString(), anyString(), any(Duration.class));

        Assertions.assertDoesNotThrow(() -> userService.login(request));
    }

    @Test
    @DisplayName("요청한 이메일로 가입된 유저를 찾을 수 없어 로그인을 할 수 없다")
    void loginWithUnregisteredUser(){
        UserLoginRequest request = new UserLoginRequest("test@test.com", "encodedPassword");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        GlobalException e = Assertions.assertThrows(GlobalException.class, () -> userService.login(request));
        Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.NOT_FOUND);
        Assertions.assertEquals(e.getErrorCode().getMessage(), "가입된 유저가 아닙니다");
    }

    @Test
    @DisplayName("요청한 비밀번호가 유저 정보와 맞지 않아 로그인을 할 수 없다")
    void loginWithInvalidPassword(){
        UserLoginRequest request = new UserLoginRequest("test@test.com", "password");
        User registedUser = UserFixture.get("test@test.com", "encodedPassword", "닉네임");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(registedUser));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        GlobalException e = Assertions.assertThrows(GlobalException.class, () -> userService.login(request));
        Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.UNAUTHORIZED);
        Assertions.assertEquals(e.getErrorCode().getMessage(), "유효한 패스워드가 아닙니다");
    }

}
