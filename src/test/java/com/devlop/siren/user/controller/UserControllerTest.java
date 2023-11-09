package com.devlop.siren.user.controller;

import com.devlop.siren.domain.user.controller.UserController;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.dto.UserTokenDto;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.dto.request.UserRoleChangeRequest;
import com.devlop.siren.domain.user.dto.response.UserReadResponse;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.fixture.UserFixture;
import com.devlop.siren.global.util.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtils utils;

    @Test
    @DisplayName("요청한 정보로 유저 회원가입에 성공한다")
    @WithMockUser
    void register() throws Exception {
        UserRegisterRequest request = UserFixture.get("test@test.com", "password");

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("유저 회원가입 시 필수값 검증에서 예외가 발생하여 회원가입에 실패한다")
    @WithMockUser
    void registerWithBlankString() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("")
                .build();
        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("유저 회원가입 시 비밀번호가 8자 미만이라 회원가입에 실패한다")
    @WithMockUser
    void registerWithShortPassword() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@test.com")
                .password("pwd")
                .nickName("닉네임")
                .phone("010-0000-0000")
                .build();

        MvcResult result = mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertThat(result.getResolvedException().getMessage()).contains("비밀번호는 8자 이상이어야 합니다");
    }

    @Test
    @DisplayName("유저 회원가입 시 닉네임이 한글이 아니라서 회원가입에 실패한다")
    @WithMockUser
    void registerWithNotKoreanNickName() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@test.com")
                .password("password")
                .nickName("nm")
                .phone("010-0000-0000")
                .build();

        MvcResult result = mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertThat(result.getResolvedException().getMessage().contains("닉네임은 한글로만 설정할 수 있습니다"));
    }

    @Test
    @DisplayName("유저 로그인에 성공한다")
    @WithMockUser
    void login() throws Exception {
        UserLoginRequest request = new UserLoginRequest("test@test.com", "password");

        mockMvc.perform(post("/api/users/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("유저 로그인 시 필수값 검증에서 예외가 발생하여 회원가입에 실패한다")
    @WithMockUser
    void loginWithBlankString() throws Exception {
        UserLoginRequest request = new UserLoginRequest("", "");
        mockMvc.perform(post("/api/users/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("이미 로그인 된 유저를 로그아웃 처리한다")
    @WithMockUser
    void logout() throws Exception {
        mockMvc.perform(patch("/api/users/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("요청헤더에 인증 정보가 없어서 로그아웃 할 수 없다")
    @WithAnonymousUser
    void logoutWithNotAuthHeader() throws Exception {
        mockMvc.perform(patch("/api/users/sessions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();
    }
    @Test
    @DisplayName("토큰이 만료되어 토큰을 재발행한다")
    @WithMockUser
    void reissue() throws Exception {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        UserTokenDto dto = new UserTokenDto(accessToken, refreshToken);
        String newAccessToken = "newAccessToken";

        when(utils.resolveToken(any(HttpServletRequest.class))).thenReturn(dto);
        when(userService.reissueAccessToken(eq(refreshToken), any(HttpServletResponse.class)))
                .thenReturn(newAccessToken);

        mockMvc.perform(patch("/api/users/sessions/renew")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .header("Refresh", refreshToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("요청헤더에 인증 정보가 없어서 토큰을 재발행 할 수 없다")
    @WithAnonymousUser
    void reissueWithNotAuthHeader() throws Exception {
        mockMvc.perform(patch("/api/users/sessions/renew")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자가 요청한 정보로 유저 권한을 수정한다")
    @WithMockUser
    void changeRole() throws Exception {
        UserRoleChangeRequest request = new UserRoleChangeRequest("test@test.com", "STAFF");
        when(userService.changeRole(request, mock(UserDetailsDto.class))).thenReturn(mock(UserReadResponse.class));
        mockMvc.perform(patch("/api/users/role")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("유저 권한 변경 시 필수 요청값이 없어 예외가 발생한다")
    @WithMockUser
    void changeRoleWithNotRequest() throws Exception {
        UserLoginRequest request = new UserLoginRequest("test@test.com", "");
        mockMvc.perform(patch("/api/users/role")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
