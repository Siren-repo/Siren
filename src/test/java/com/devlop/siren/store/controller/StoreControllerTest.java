package com.devlop.siren.store.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devlop.siren.domain.store.controller.StoreController;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.dto.request.StoreRegisterRequest;
import com.devlop.siren.domain.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.fixture.UserFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(StoreController.class)
public class StoreControllerTest {

  @Autowired MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean StoreService storeService;

  @Mock private Store mockStore1;

  @BeforeEach
  public void init() {
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    UserDetailsDto dto = UserFixture.get(UserRole.ADMIN);

    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));

    mockStore1 =
        Store.builder()
            .storeId(1L)
            .storeName("Store Name 1")
            .storePhone("Store Phone 1")
            .city("Store City 1")
            .street("Store Street 1")
            .zipCode("12345")
            .closeTime(LocalTime.of(18, 0))
            .openTime(LocalTime.of(9, 0))
            .latitude(37.48428)
            .longitude(126.9015)
            .build();
  }

  @Test
  @DisplayName("어드민이 매장을 등록한다")
  void 매장_등록_성공() throws Exception {
    StoreRegisterRequest storeRegisterRequest =
        StoreRegisterRequest.builder()
            .storeName("A TWOSOME PLACE2")
            .storePhone("010101010")
            .city("Seoul")
            .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
            .zipCode("54321")
            .closeTime(LocalTime.of(18, 0))
            .openTime(LocalTime.of(9, 0))
            .build();

    mockMvc
        .perform(
            post("/api/stores")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRegisterRequest)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("어드민이 아닌 유저라 매장 등록에 실패한다")
  void 매장_등록_실패_권한실패() throws Exception {
    UserDetailsDto dto = UserFixture.get(UserRole.STAFF);
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));
    StoreRegisterRequest storeRegisterRequest =
        StoreRegisterRequest.builder()
            .storeName("A TWOSOME PLACE2")
            .storePhone("010101010")
            .city("Seoul")
            .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
            .zipCode("54321")
            .closeTime(LocalTime.of(18, 0))
            .openTime(LocalTime.of(9, 0))
            .build();
    mockMvc
        .perform(
            post("/api/stores")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRegisterRequest)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @DisplayName("매장 의 오픈 시간과 닫는 시간은 null 일 수있다 성공.")
  void 매장_등록_매장시간_성공() throws Exception {
    StoreRegisterRequest storeRegisterRequest =
        StoreRegisterRequest.builder()
            .storeName("test")
            .storePhone("010101010")
            .city("Seoul")
            .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
            .zipCode("54321")
            .build();
    mockMvc
        .perform(
            post("/api/stores")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRegisterRequest)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("매장 의 이름을 등록하지 않았을 경우 실패.")
  void 매장_등록_실패() throws Exception {
    StoreRegisterRequest storeRegisterRequest =
        StoreRegisterRequest.builder()
            .storeName("")
            .storePhone("010101010")
            .city("Seoul")
            .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
            .zipCode("54321")
            .closeTime(LocalTime.of(18, 0))
            .openTime(LocalTime.of(9, 0))
            .build();

    mockMvc
        .perform(
            post("/api/stores")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRegisterRequest)))
        .andExpect(status().isBadRequest()) //
        .andDo(print());
  }

  @Test
  @DisplayName("매장 의 우편 번호는 5글자 이상 일 경우 실패.")
  void 매장_등록_우편번호_실패() throws Exception {
    StoreRegisterRequest storeRegisterRequest =
        StoreRegisterRequest.builder()
            .storeName("test")
            .storePhone("010101010")
            .city("Seoul")
            .street("서울 구로구 디지털로32길 97-39 2층 (우)08391")
            .zipCode("543211234")
            .closeTime(LocalTime.of(18, 0))
            .openTime(LocalTime.of(9, 0))
            .build();

    mockMvc
        .perform(
            post("/api/stores")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRegisterRequest)))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("매장 삭제")
  void deleteItem(Long storeId) throws Exception {
    mockMvc
        .perform(delete("/api/stores/{storeId}", storeId).with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @ParameterizedTest
  @ValueSource(longs = {1L, 2L})
  @DisplayName("권한이 없어 매장 삭제에 실패한다")
  void deleteItemWithNoAuth(Long storeId) throws Exception {
    UserDetailsDto dto = UserFixture.get(UserRole.STAFF);
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));
    mockMvc
        .perform(delete("/api/stores/{storeId}", storeId).with(csrf()))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @DisplayName("매장 상세 정보 조회")
  @ParameterizedTest
  @WithMockUser
  @ValueSource(longs = {1L, 2L})
  void detailsStore(Long storeId) throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/stores/details/{storeId}", storeId).with(csrf()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("모든 매장 정보 조회")
  @WithMockUser
  void getAllStore() throws Exception {
    List<Store> mockStores = Collections.singletonList(mockStore1);
    when(storeService.getAllStores()).thenReturn(mockStores);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/stores/all").contentType(MediaType.APPLICATION_JSON))
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

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/stores/nearby")
                .param("latitude", String.valueOf(latitude))
                .param("longitude", String.valueOf(longitude))
                .param("radiusKm", String.valueOf(radiusKm)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("매장 정보 업데이트")
  void updateStore() throws Exception {
    Long storeId = 1L;
    StoreUpdateRequest storeUpdateRequest =
        new StoreUpdateRequest(
            "Updated Store Name",
            "Updated Store Phone",
            "Updated City",
            "Updated Street",
            "12345",
            LocalTime.of(9, 0),
            LocalTime.of(18, 0));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/stores/{storeId}", storeId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeUpdateRequest)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("권한이 없어 매장 정보 업데이트에 실패한다")
  void updateStoreWithNotAuth() throws Exception {
    Long storeId = 1L;
    UserDetailsDto dto = UserFixture.get(UserRole.STAFF);
    SecurityContextHolder.getContext()
        .setAuthentication(
            new UsernamePasswordAuthenticationToken(dto, null, dto.getAuthorities()));
    StoreUpdateRequest storeUpdateRequest =
        new StoreUpdateRequest(
            "Updated Store Name",
            "Updated Store Phone",
            "Updated City",
            "Updated Street",
            "12345",
            LocalTime.of(9, 0),
            LocalTime.of(18, 0));
    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/api/stores/{storeId}", storeId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeUpdateRequest)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }
}
