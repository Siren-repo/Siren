package com.devlop.siren.unit.controller;

import com.devlop.siren.domain.user.controller.UserController;
import com.devlop.siren.domain.user.dto.request.UserRegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @InjectMocks
    private UserController restController;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(restController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("유저 회원가입 시 필수값 검증에서 예외가 발생하여 회원가입에 실패한다")
    void registerWithBlankString() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("")
                .build();
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("유저 회원가입 시 비밀번호가 8자 미만이라 회원가입에 실패한다")
    void registerWithShortPassword() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@test.com")
                .password("pwd")
                .nickName("닉네임")
                .phone("010-0000-0000")
                .build();

        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertThat(result.getResolvedException().getMessage()).contains("비밀번호는 8자 이상이어야 합니다");
    }

    @Test
    @DisplayName("유저 회원가입 시 닉네임이 한글이 아니라서 회원가입에 실패한다")
    void registerWithNotKoreanNickName() throws Exception {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("test@test.com")
                .password("password")
                .nickName("nm")
                .phone("010-0000-0000")
                .build();

        MvcResult result = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertThat(result.getResolvedException().getMessage().contains("닉네임은 한글로만 설정할 수 있습니다"));
    }
}
