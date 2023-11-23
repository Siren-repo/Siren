package com.devlop.siren.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserTokenDto;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.domain.user.service.RedisService;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import java.time.Duration;
import java.util.EnumSet;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private BCryptPasswordEncoder encoder;
  @Mock private AllergyConverter converter;
  @Mock private HttpServletResponse response;
  @Mock private RedisService redisService;
  @Mock private JwtTokenUtils utils;
  @InjectMocks private UserService userService;

  @BeforeEach
  public void setUp() {
    ReflectionTestUtils.setField(userService, "accessExpiredTimeMs", 60000L);
    ReflectionTestUtils.setField(userService, "refreshExpiredTimeMs", 604800000L);
    ReflectionTestUtils.setField(
        userService, "secretKey", "2023-wanted-internship-siren-order-project.secret_key");
    ReflectionTestUtils.setField(
        utils, "secretKey", "2023-wanted-internship-siren-order-project.secret_key");
  }

  @Test
  @DisplayName("요청한 내용으로 회원가입한다")
  void register() {
    UserRegisterRequest request = UserFixture.get("email", "encodedPassword");
    EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);

    when(encoder.encode(request.getPassword())).thenReturn("encodedPassword");
    when(converter.convertToEntityAttribute(request.getAllergies())).thenReturn(allergies);

    userService.register(request);

    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("이메일이 중복되어 요청한 정보로 회원가입을 할 수 없다")
  void registerWithDuplicatedUser() {
    UserRegisterRequest request = UserFixture.get("email", "password");
    EnumSet<AllergyType> allergies = EnumSet.of(AllergyType.PEANUT, AllergyType.MILK);
    User duplicatedUser =
        UserRegisterRequest.fromDto(request, "encodedPassword", UserRole.CUSTOMER, allergies);

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(duplicatedUser));
    GlobalException e = assertThrows(GlobalException.class, () -> userService.register(request));

    Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.CONFLICT);
  }

  @Test
  @DisplayName("요청한 이메일과 패스워드로 로그인한다")
  void login() {
    UserLoginRequest request = new UserLoginRequest("test@test.com", "encodedPassword");
    User registedUser = UserFixture.get("test@test.com", "encodedPassword", "닉네임");

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(registedUser));
    when(encoder.matches(request.getPassword(), registedUser.getPassword())).thenReturn(true);
    doNothing().when(redisService).setValue(anyString(), anyString(), any(Duration.class));

    Assertions.assertDoesNotThrow(() -> userService.login(request, response));
  }

  @Test
  @DisplayName("요청한 이메일로 가입된 유저를 찾을 수 없어 로그인을 할 수 없다")
  void loginWithUnregisteredUser() {
    UserLoginRequest request = new UserLoginRequest("test@test.com", "encodedPassword");

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

    GlobalException e =
        assertThrows(GlobalException.class, () -> userService.login(request, response));
    Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.NOT_FOUND);
    Assertions.assertEquals(e.getErrorCode().getMESSAGE(), "가입된 유저가 아닙니다");
  }

  @Test
  @DisplayName("요청한 비밀번호가 유저 정보와 맞지 않아 로그인을 할 수 없다")
  void loginWithInvalidPassword() {
    UserLoginRequest request = new UserLoginRequest("test@test.com", "password");
    User registedUser = UserFixture.get("test@test.com", "encodedPassword", "닉네임");

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(registedUser));
    when(encoder.matches(anyString(), anyString())).thenReturn(false);

    GlobalException e =
        assertThrows(GlobalException.class, () -> userService.login(request, response));
    Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.UNAUTHORIZED);
    Assertions.assertEquals(e.getErrorCode().getMESSAGE(), "유효한 패스워드가 아닙니다");
  }

  @Test
  @DisplayName("저장중인 리프레시 토큰으로 또 로그인을 요청한 경우 로그인 할 수 없다")
  void loginWithAlreadySavedRefreshToken() {
    UserLoginRequest request = new UserLoginRequest("test@test.com", "password");
    User registedUser = UserFixture.get("test@test.com", "encodedPassword", "닉네임");

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(registedUser));
    when(encoder.matches(anyString(), anyString())).thenReturn(true);
    when(redisService.existRefreshToken(anyString())).thenReturn(true);

    GlobalException e =
        assertThrows(GlobalException.class, () -> userService.login(request, response));
    Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.UNAUTHORIZED);
    Assertions.assertEquals(e.getErrorCode().getMESSAGE(), "이미 로그인 된 계정입니다");
  }

  @Test
  @DisplayName("로그인 중인 계정을 로그아웃 한다 - 리프레시 토큰 삭제 및 엑세스 토큰 로그아웃 처리")
  void logout() {
    UserTokenDto dto = new UserTokenDto("accessToken", "refreshToken");
    String requestEmail = "test@test.com";
    Claims mockClaims = mock(Claims.class);
    when(mockClaims.getSubject()).thenReturn(requestEmail);

    try (MockedStatic<JwtTokenUtils> jwtUtilsMock = mockStatic(JwtTokenUtils.class)) {
      jwtUtilsMock.when(() -> JwtTokenUtils.extractClaims(any())).thenReturn(mockClaims);

      when(redisService.existRefreshToken(requestEmail)).thenReturn(true);
      userService.logout(dto);

      verify(redisService).deleteValue(requestEmail);
      verify(redisService).setValue(eq(dto.getAccessToken()), eq("logout"), any(Duration.class));
    }
  }

  @Test
  @DisplayName("요청한 토큰의 subject를 추출할 수 없어 로그아웃 처리를 할 수 없다")
  void logoutWithNotExtractRefreshToken() {
    UserTokenDto dto = new UserTokenDto("accessToken", "refreshToken");

    try (MockedStatic<JwtTokenUtils> jwtUtilsMock = mockStatic(JwtTokenUtils.class)) {
      jwtUtilsMock
          .when(() -> JwtTokenUtils.extractClaims(any(String.class)))
          .thenThrow(MalformedJwtException.class);

      assertThrows(MalformedJwtException.class, () -> userService.logout(dto));
    }
  }

  @Test
  @DisplayName("요청한 계정의 리프레시 토큰을 찾을 수 없어 로그아웃 처리를 할 수 없다")
  void logoutWithExpiredRefreshToken() {
    UserTokenDto dto = new UserTokenDto("accessToken", "refreshToken");
    String requestEmail = "test@test.com";
    Claims mockClaims = mock(Claims.class);
    when(mockClaims.getSubject()).thenReturn(requestEmail);

    try (MockedStatic<JwtTokenUtils> jwtUtilsMock = mockStatic(JwtTokenUtils.class)) {
      jwtUtilsMock.when(() -> JwtTokenUtils.extractClaims(any())).thenReturn(mockClaims);

      when(redisService.existRefreshToken(requestEmail)).thenReturn(false);
      GlobalException e = assertThrows(GlobalException.class, () -> userService.logout(dto));
      Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.UNAUTHORIZED);
      Assertions.assertEquals(e.getErrorCode().getMESSAGE(), "리프레시 토큰이 만료되었습니다");
    }
  }

  @Test
  @DisplayName("토큰을 재발행한다")
  void reissue() {
    String requestEmail = "test@test.com";
    String refreshToken = "refreshToken";
    String newAccessToken = "newAccessToken";
    Claims mockClaims = mock(Claims.class);
    when(mockClaims.getSubject()).thenReturn(requestEmail);

    try (MockedStatic<JwtTokenUtils> jwtUtilsMock = mockStatic(JwtTokenUtils.class)) {
      jwtUtilsMock.when(() -> JwtTokenUtils.extractClaims(any())).thenReturn(mockClaims);
      jwtUtilsMock
          .when(() -> JwtTokenUtils.generateAccessToken(any(), any(), any()))
          .thenReturn(newAccessToken);

      when(redisService.existRefreshToken(requestEmail)).thenReturn(true);
      when(userRepository.findByEmail(requestEmail)).thenReturn(Optional.of(mock(User.class)));

      Assertions.assertDoesNotThrow(() -> userService.reissueAccessToken(refreshToken, response));
    }
  }

  @Test
  @DisplayName("요청한 리프레시 토큰에 해당하는 유저를 찾을 수 없어 토큰 재발행을 할 수 없다")
  void reissueWithNotFoundUser() {
    String requestEmail = "test@test.com";
    String refreshToken = "refreshToken";
    Claims mockClaims = mock(Claims.class);
    when(mockClaims.getSubject()).thenReturn(requestEmail);

    try (MockedStatic<JwtTokenUtils> jwtUtilsMock = mockStatic(JwtTokenUtils.class)) {
      jwtUtilsMock.when(() -> JwtTokenUtils.extractClaims(any())).thenReturn(mockClaims);
      when(redisService.existRefreshToken(requestEmail)).thenReturn(true);
      when(userRepository.findByEmail(requestEmail)).thenReturn(Optional.empty());

      GlobalException e =
          assertThrows(
              GlobalException.class, () -> userService.reissueAccessToken(refreshToken, response));
      Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.NOT_FOUND);
      Assertions.assertEquals(e.getErrorCode().getMESSAGE(), "가입된 유저가 아닙니다");
    }
  }

  @Test
  @DisplayName("요청한 리프레시 토큰이 만료되어 토큰 재발행을 할 수 없다")
  void reissueWithNotExistsRefreshToken() {
    String requestEmail = "test@test.com";
    String refreshToken = "refreshToken";
    String newAccessToken = "newAccessToken";
    Claims mockClaims = mock(Claims.class);
    when(mockClaims.getSubject()).thenReturn(requestEmail);

    try (MockedStatic<JwtTokenUtils> jwtUtilsMock = mockStatic(JwtTokenUtils.class)) {
      jwtUtilsMock.when(() -> JwtTokenUtils.extractClaims(any())).thenReturn(mockClaims);
      jwtUtilsMock
          .when(() -> JwtTokenUtils.generateAccessToken(any(), any(), any()))
          .thenReturn(newAccessToken);

      when(redisService.existRefreshToken(requestEmail)).thenReturn(false);

      GlobalException e =
          assertThrows(
              GlobalException.class, () -> userService.reissueAccessToken(refreshToken, response));
      Assertions.assertEquals(e.getErrorCode().getStatus(), HttpStatus.UNAUTHORIZED);
      Assertions.assertEquals(e.getErrorCode().getMESSAGE(), "리프레시 토큰이 만료되었습니다");
    }
  }
}
