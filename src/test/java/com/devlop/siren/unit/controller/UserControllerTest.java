package com.devlop.siren.unit.controller;

import com.devlop.siren.domain.user.controller.UserController;
import com.devlop.siren.domain.user.dto.request.UserLoginRequest;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("요청한 정보로 유저 회원가입에 성공한다")
    @WithMockUser
    void register() throws Exception {
        UserRegisterRequest request = UserFixture.get("test@test.com", "password");

        mockMvc.perform(post("/api/users/register")
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
        mockMvc.perform(post("/api/users/register")
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

        MvcResult result = mockMvc.perform(post("/api/users/register")
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

        MvcResult result = mockMvc.perform(post("/api/users/register")
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

        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
}
