package com.devlop.siren.store.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.domain.store.service.StoreService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StoreGetNearByStoreTest {
    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private Store mockStore1;
    @Mock
    private Store mockStore2;
    @Mock
    private Store mockStore3;
    @Mock
    private Store mockStore4;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockStore1 = Store.builder()
                .storeId(1L)
                .storeName("Store Name 1")
                .storePhone("Store Phone 1")
                .city("Store City 1")
                .street("Store Street 1")
                .zipCode(12345)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .latitude(37.48428)
                .longitude(126.9015)
                .build();

        // 위로 1km
        mockStore2 = Store.builder()
                .storeId(2L)
                .storeName("Store Name 2")
                .storePhone("Store Phone 2")
                .city("Store City 2")
                .street("Store Street 2")
                .zipCode(23455)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .latitude(37.48428 + 0.0009)
                .longitude(126.9015)
                .build();

        // 1km 밖에 있는 매장 3, 4

        mockStore3 = Store.builder()
                .storeId(3L)
                .storeName("Store Name 3")
                .storePhone("Store Phone 3")
                .city("Store City 3")
                .street("Store Street 3")
                .zipCode(34567)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .latitude(37.47428)
                .longitude(126.9015)
                .build();

        //37.46884, 126.8973
        mockStore4 = Store.builder()
                .storeId(4L)
                .storeName("Store Name def")
                .storePhone("Store Phone def")
                .city("Store City def")
                .street("Store Street def")
                .zipCode(87654)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .latitude(37.46884)
                .longitude(126.8973)
                .build();

    }

    @Test
    @DisplayName("주변 2KM 에 저장한 총 4개 나와야 함")
    public void 주변_매장_2KM() {
        // Given
        when(storeRepository.findAll()).thenReturn(Arrays.asList(mockStore1, mockStore2, mockStore3, mockStore4));
        double centerLatitude = 37.48428;
        double centerLongitude = 126.9015;
        double radiusInKm = 2;

        // When
        List<Store> nearbyStores = storeService.getNearbyStores(centerLatitude, centerLongitude, radiusInKm);

        // Then
        assertEquals(4, nearbyStores.size()); // 2 면 에러

        Store nearbyStore1 = nearbyStores.get(0);
        assertEquals(Long.valueOf(1L), nearbyStore1.getStoreId());
        assertEquals("Store Name 1", nearbyStore1.getStoreName());
        assertEquals("Store Phone 1", nearbyStore1.getStorePhone());
        assertEquals("Store City 1", nearbyStore1.getCity());
        assertEquals("Store Street 1", nearbyStore1.getStreet());
        assertEquals(Integer.valueOf(12345), nearbyStore1.getZipCode());

        Store nearbyStore2 = nearbyStores.get(1);
        assertEquals(Long.valueOf(2L), nearbyStore2.getStoreId());
        assertEquals("Store Name 2", nearbyStore2.getStoreName());
        assertEquals("Store Phone 2", nearbyStore2.getStorePhone());
        assertEquals("Store City 2", nearbyStore2.getCity());
        assertEquals("Store Street 2", nearbyStore2.getStreet());
        assertEquals(Integer.valueOf(23455), nearbyStore2.getZipCode());
    }

    @Test
    @DisplayName("주변 1KM 에 저장한 총 2개 나와야 함")
    public void 주변_매장_1KM() {
        // Given
        when(storeRepository.findAll()).thenReturn(Arrays.asList(mockStore1, mockStore2, mockStore3, mockStore4));
        double centerLatitude = 37.48428;
        double centerLongitude = 126.9015;
        double radiusInKm = 1;

        // When
        List<Store> nearbyStores = storeService.getNearbyStores(centerLatitude, centerLongitude, radiusInKm);

        // Then
        assertEquals(2, nearbyStores.size());

        Store nearbyStore1 = nearbyStores.get(0);
        assertEquals(Long.valueOf(1L), nearbyStore1.getStoreId());
        assertEquals("Store Name 1", nearbyStore1.getStoreName());
        assertEquals("Store Phone 1", nearbyStore1.getStorePhone());
        assertEquals("Store City 1", nearbyStore1.getCity());
        assertEquals("Store Street 1", nearbyStore1.getStreet());
        assertEquals(Integer.valueOf(12345), nearbyStore1.getZipCode());

        Store nearbyStore2 = nearbyStores.get(1);
        assertEquals(Long.valueOf(2L), nearbyStore2.getStoreId());
        assertEquals("Store Name 2", nearbyStore2.getStoreName());
        assertEquals("Store Phone 2", nearbyStore2.getStorePhone());
        assertEquals("Store City 2", nearbyStore2.getCity());
        assertEquals("Store Street 2", nearbyStore2.getStreet());
        assertEquals(Integer.valueOf(23455), nearbyStore2.getZipCode());
    }
}
