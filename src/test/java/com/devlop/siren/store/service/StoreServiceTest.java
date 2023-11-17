package com.devlop.siren.store.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.dto.request.StoreRegisterRequest;
import com.devlop.siren.domain.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.domain.store.utils.GeocodingApi;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.exception.GlobalException;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    @InjectMocks
    private StoreService storeService;

    @Mock
    private GeocodingApi geocodingApi;
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreRegisterRequest registerRequest;

    @Mock
    private Store mockStore;
    @Mock
    private UserDetailsDto admin;
    @Mock
    private UserDetailsDto customer;

    @BeforeEach
    void setUp() {
        admin = new UserDetailsDto(1L, "test@test.com", "testtest", UserRole.ADMIN, false);
        customer = new UserDetailsDto(2L, "test@test.com", "testtest", UserRole.CUSTOMER, false);

        registerRequest = new StoreRegisterRequest("First Store Name",
                "First Store Phone", "Seoul", "대전 서구 둔산중로32번길 29 1층 103호",
                54321, LocalDateTime.of(2023, 9, 25, 18, 0),
                LocalDateTime.of(2023, 9, 25, 9, 0));

        mockStore = Store.builder()
                .storeId(1L)
                .storeName("First Store Name")
                .storePhone("First Store Phone")
                .city("Seoul")
                .street("대전 서구 둔산중로32번길 29 1층 103호")
                .zipCode(54321)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();
    }

    @Test
    @DisplayName("매장 등록에 성공 (권한 : ADMIN)")
    void registerStore() throws IOException, InterruptedException, ApiException {
        GeocodingResult mockGeocodingResult = new GeocodingResult();
        mockGeocodingResult.geometry = new Geometry();
        mockGeocodingResult.geometry.location = new LatLng(37.1234, -122.5678);

        when(geocodingApi.geocodeAddress(any(String.class)))
                .thenReturn(
                        new GeocodingResult[]{
                                mockGeocodingResult
                        });

        storeService.registerStore(registerRequest, admin);

        verify(storeRepository).save(any(Store.class));

    }

    @Test
    @DisplayName("매장 등록에 실패한다. (지오코딩 정보 null)")
    void registerStoreGeocodingNull() throws IOException, InterruptedException, ApiException {

        when(geocodingApi.geocodeAddress(any(String.class)))
                .thenReturn(new GeocodingResult[0]);

        GlobalException exception = assertThrows(GlobalException.class,
                () -> storeService.registerStore(registerRequest, admin)
        );

        assertEquals(exception.getErrorCode().getMESSAGE(), "지오코딩 위도 경도 조회시 에러발생");

    }

    @Test
    @DisplayName("매장 등록 실패 (권한 CUSTOMER) ")
    void registerStoreWithCustomer() {

        GlobalException exception = assertThrows(GlobalException.class,
                () -> storeService.registerStore(registerRequest, customer)
        );

        assertEquals(exception.getErrorCode().getMESSAGE(), "권한이 없는 사용자입니다");

    }

    @Test
    @DisplayName("매장 업데이트 성공 (권한 : ADMIN)")
    void updateStore() {
        Long storeId = 1L;
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest(
                "Updated Store Name", "Updated Store Phone",
                "Updated City", "Updated Street", 12345,
                LocalDateTime.of(2023, 9, 25, 9, 0),
                LocalDateTime.of(2023, 9, 25, 18, 0)
        );

        when(storeRepository.findByStoreId(1L))
                .thenReturn(Optional.of(mockStore)
                );

        storeService.updateStore(storeId, storeUpdateRequest, admin);

        Assert.assertEquals("Updated Store Name", mockStore.getStoreName());
        Assert.assertEquals("Updated Store Phone", mockStore.getStorePhone());
        Assert.assertEquals("Updated City", mockStore.getCity());
        Assert.assertEquals("Updated Street", mockStore.getStreet());
        Assert.assertEquals(Integer.valueOf(12345), mockStore.getZipCode());

    }

    @Test
    @DisplayName("매장 업데이트 실패 (권한 : CUSTOMER)")
    void updateStoreWithCustomer() {
        Long storeId = 1L;
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest(
                "Updated Store Name", "Updated Store Phone",
                "Updated City", "Updated Street", 12345,
                LocalDateTime.of(2023, 9, 25, 9, 0),
                LocalDateTime.of(2023, 9, 25, 18, 0)
        );

        GlobalException exception = assertThrows(GlobalException.class,
                () -> storeService.updateStore(storeId, storeUpdateRequest, customer)
        );

        assertEquals(exception.getErrorCode().getMESSAGE(), "권한이 없는 사용자입니다");

    }

    @Test
    @DisplayName("없는 매장 업데이트 시도 (권한 : ADMIN)")
    void updateStoreWithAdminNull() {
        Long storeId = 2L;
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest(
                "Updated Store Name", "Updated Store Phone",
                "Updated City", "Updated Street", 12345,
                LocalDateTime.of(2023, 9, 25, 9, 0),
                LocalDateTime.of(2023, 9, 25, 18, 0)
        );

        GlobalException exception = assertThrows(GlobalException.class,
                () -> storeService.updateStore(storeId, storeUpdateRequest, admin)
        );

        assertEquals(exception.getErrorCode().getMESSAGE(), "매장이 존재하지 않습니다.");

    }

    @Test
    @DisplayName("부분_매장_업데이트 (권한 : ADMIN) ")
    public void partialUpdate() {
        //given
        Long storeId = 1L;
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest
                ("Updated Store Name", null, null, null, null, null, null);

        when(storeRepository.findByStoreId(storeId))
                .thenReturn(Optional.of(mockStore)
                );

        storeService.updateStore(storeId, storeUpdateRequest, admin);

        //then
        Assert.assertEquals("Updated Store Name", mockStore.getStoreName());
        Assert.assertEquals("First Store Phone", mockStore.getStorePhone());
        Assert.assertEquals("Seoul", mockStore.getCity());
        Assert.assertEquals("대전 서구 둔산중로32번길 29 1층 103호", mockStore.getStreet());
        Assert.assertEquals(Integer.valueOf("54321"), mockStore.getZipCode());


    }

    @Test
    @DisplayName("매장 삭제 성공 (권한 : ADMIN) ")
    void deleteStore() {

        Long storeId = 1L;

        when(storeRepository.findByStoreId(storeId))
                .thenReturn(Optional.of(mockStore)
                );

        Long deletedStoreId = storeService.deleteStore(storeId, admin);

        assertEquals(storeId, deletedStoreId);
    }

    @Test
    @DisplayName("매장 삭제 실패 (권한 : CUSTOMER) ")
    void deleteStoreWithCustomer() {
        Long storeId = 1L;
        GlobalException exception = assertThrows(GlobalException.class,
                () -> storeService.deleteStore(storeId, customer)
        );

        assertEquals(exception.getErrorCode().getMESSAGE(), "권한이 없는 사용자입니다");
    }

    @Test
    @DisplayName("존재 하지 않는 매장 삭제 (권한 : ADMIN) ")
    void deleteStoreWithAdminNull() {

        Long storeId = 2L;

        when(storeRepository.findByStoreId(storeId))
                .thenReturn(Optional.empty()
                );

        GlobalException exception = assertThrows(GlobalException.class,
                () -> storeService.deleteStore(storeId, admin)
        );

        assertEquals(exception.getErrorCode().getMESSAGE(), "매장이 존재하지 않습니다.");
    }

}