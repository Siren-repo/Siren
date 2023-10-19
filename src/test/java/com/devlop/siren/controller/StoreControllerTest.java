package com.devlop.siren.controller;


import com.devlop.siren.store.controller.StoreController;
import com.devlop.siren.store.request.StoreRegisterRequest;
import com.devlop.siren.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    StoreService storeService;
    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        //테스트시 LocalDateTime 에러 발생하여 모듈 추가
        objectMapper.registerModule(new JavaTimeModule());
    }

    @ParameterizedTest
    @ValueSource(strings = {"102"})
    @DisplayName("매장 정상 등록시 성공.")
    void 매장_등록_성공(String role) throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("ATWOSOME PLACE2")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(54321)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

        mockMvc.perform(post("/api/stores/register/{role}",role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @ParameterizedTest
    @ValueSource(strings = {"102"})
    @DisplayName("매장 의 오픈 시간과 닫는 시간은 null 일 수있다 성공.")
    void 매장_등록_매장시간_성공(String role) throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("test")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(54321)
                .build();

        mockMvc.perform(post("/api/stores/register/{role}",role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isOk()) //
                .andDo(print());
    }
    @ParameterizedTest
    @ValueSource(strings = {"102"})
    @DisplayName("매장 의 이름을 등록하지 않았을 경우 실패.")
    void 매장_등록_실패(String role) throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(54321)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

        mockMvc.perform(post("/api/stores/register/{role}",role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isBadRequest()) //
                .andDo(print());
    }
    @ParameterizedTest
    @ValueSource(strings = {"102"})
    @DisplayName("매장 의 우편 번호는 5글자 이상 일 경우 실패.")
    void 매장_등록_우편번호_실패(String role) throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("test")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(543211234)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

        mockMvc.perform(post("/api/stores/register/{role}",role)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isNotAcceptable()) //
                .andDo(print());
    }
    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    @DisplayName("정상적인 삭제 의 경우")
    void deleteItem(Long storeId) throws Exception {
        mockMvc.perform(delete("/api/stores/delete/{storeId}", storeId))
                .andExpect(status().isOk())
                .andDo(print());
    }



}
