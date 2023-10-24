package com.devlop.siren.store.controller;


import com.devlop.siren.domain.store.controller.StoreController;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.dto.request.StoreRegisterRequest;
import com.devlop.siren.domain.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.domain.store.service.StoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Mock
    private Store mockStore1;
    @BeforeEach
    public void init(){
        objectMapper = new ObjectMapper();
        //테스트시 LocalDateTime 에러 발생하여 모듈 추가
        objectMapper.registerModule(new JavaTimeModule());

        mockStore1 = Store.builder()
                .storeId(1L)
                .storeName("Store Name 1")
                .storePhone("Store Phone 1")
                .city("Store City 1" )
                .street("Store Street 1" )
                .zipCode(12345)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .latitude(37.48428)
                .longitude(126.9015)
                .build();
    }

    @Test
    @DisplayName("매장 정상 등록시 성공.")
    @WithMockUser
    void 매장_등록_성공() throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("ATWOSOME PLACE2")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(54321)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

        mockMvc.perform(post("/api/stores/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("매장 의 오픈 시간과 닫는 시간은 null 일 수있다 성공.")
    @WithMockUser
    void 매장_등록_매장시간_성공() throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("test")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(54321)
                .build();

        mockMvc.perform(post("/api/stores/register/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithMockUser
    @DisplayName("매장 의 이름을 등록하지 않았을 경우 실패.")
    void 매장_등록_실패() throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(54321)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

        mockMvc.perform(post("/api/stores/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isBadRequest()) //
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("매장 의 우편 번호는 5글자 이상 일 경우 실패.")
    void 매장_등록_우편번호_실패() throws Exception {
        StoreRegisterRequest storeRegisterRequest = StoreRegisterRequest.builder()
                .storeName("test")
                .storePhone("010101010" )
                .city("Seoul")
                .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
                .zipCode(543211234)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

        mockMvc.perform(post("/api/stores/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRegisterRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
    @ParameterizedTest
    @WithMockUser
    @ValueSource(longs = {1L, 2L})
    @DisplayName("정상적인 삭제 의 경우")
    void deleteItem(Long storeId) throws Exception {
        mockMvc.perform(delete("/api/stores/delete/{storeId}", storeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("매장 상세 정보 조회")
    @ParameterizedTest
    @WithMockUser
    @ValueSource(longs = {1L, 2L})
    void detailsStore(Long storeId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/details/{storeId}",storeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("모든 매장 정보 조회")
    @WithMockUser
    void getAllStore() throws Exception {
        List<Store> mockStores = Collections.singletonList(mockStore1);
        when(storeService.getAllStores()).thenReturn(mockStores);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("주변 매장 조회")
    @WithMockUser
    void findNearbyStores() throws Exception {
        Double latitude = 12.345;
        Double longitude = 67.890;
        Double radiusKm = 5.0;
        List<Store> nearbyStores = Collections.singletonList(mockStore1);
        when(storeService.getNearbyStores(latitude, longitude, radiusKm)).thenReturn(nearbyStores);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/stores/nearby")
                        .param("latitude", String.valueOf(latitude))
                        .param("longitude", String.valueOf(longitude))
                        .param("radiusKm", String.valueOf(radiusKm))
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("매장 정보 업데이트")
    @WithMockUser
    void updateStore() throws Exception {
        Long storeId = 1L;
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest(
                "Updated Store Name", "Updated Store Phone",
                "Updated City", "Updated Street", 12345,
                LocalDateTime.of(2023, 9, 25, 9, 0),
                LocalDateTime.of(2023, 9, 25, 18, 0)
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/api/stores/update/{storeId}", storeId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeUpdateRequest))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
