package com.devlop.siren.store.service;

import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.domain.store.dto.response.StoreResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class StoreGetServiceTest {
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

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        List<Store> fakeStores = new ArrayList<>();

        for (long i = 1; i <= 3; i++) {
            Store fakeStore = Store.builder()
                    .storeId(i)
                    .storeName("Store Name " + i)
                    .storePhone("Store Phone " + i)
                    .city("Store City " + i)
                    .street("Store Street " + i)
                    .zipCode(54321 + (int) i)
                    .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                    .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                    .build();

            fakeStores.add(fakeStore);
        }
        mockStore1 = fakeStores.get(0);
        mockStore2 = fakeStores.get(1);
        mockStore3 = fakeStores.get(2);
    }
    @Test
    public void 매장_상세_조회() {
        // Given
        //
        // When
        when(storeRepository.findByStoreId(1L)).thenReturn(Optional.of(mockStore1));
        when(storeRepository.findByStoreId(2L)).thenReturn(Optional.of(mockStore2));
        when(storeRepository.findByStoreId(3L)).thenReturn(Optional.of(mockStore3));

        StoreResponse storeResponse1 = storeService.detailsStore(1L);
        StoreResponse storeResponse2 = storeService.detailsStore(2L);
        StoreResponse storeResponse3 = storeService.detailsStore(3L);

        // Then
        assertEquals("Store Name 1", storeResponse1.getStoreName());
        assertEquals("Store Phone 1", storeResponse1.getStorePhone());
        assertEquals("Store City 1", storeResponse1.getCity());
        assertEquals("Store Street 1", storeResponse1.getStreet());
        assertEquals(Integer.valueOf(54321 + 1), storeResponse1.getZipCode()); // Update with the expected zip code
        assertEquals(LocalDateTime.of(2023, 9, 25, 18, 0), storeResponse1.getOpenTime());
        assertEquals(LocalDateTime.of(2023, 9, 25, 9, 0), storeResponse1.getCloseTime());

        assertEquals("Store Name 2", storeResponse2.getStoreName());

        assertEquals("Store Name 3", storeResponse3.getStoreName());

    }

    @Test
    void 매장_전체_조회() {
        // Given
        List<Store> mockStores = Arrays.asList(mockStore1, mockStore2, mockStore3);
        when(storeRepository.findAll()).thenReturn(mockStores);

        // When
        List<Store> allStores = storeService.getAllStores();

        // Then
        assertEquals(3, allStores.size());

        Store store1 = allStores.get(0);
        assertEquals("Store Name 1", store1.getStoreName());
        assertEquals("Store Phone 1", store1.getStorePhone());
        assertEquals("Store Street 1", store1.getStreet());
        assertEquals("Store City 1", store1.getCity());
        assertEquals(Integer.valueOf(54321 +1), store1.getZipCode()); // Update with the expected zip code
        assertEquals(LocalDateTime.of(2023, 9, 25, 18, 0), store1.getOpenTime());
        assertEquals(LocalDateTime.of(2023, 9, 25, 9, 0), store1.getCloseTime());

        Store store2 = allStores.get(1);
        assertEquals("Store Name 2", store2.getStoreName());
        assertEquals("Store Phone 2", store2.getStorePhone());
        assertEquals("Store Street 2", store2.getStreet());
        assertEquals("Store City 2", store2.getCity());
        assertEquals(Integer.valueOf(54321 +2), store2.getZipCode()); // Update with the expected zip code
        assertEquals(LocalDateTime.of(2023, 9, 25, 18, 0), store2.getOpenTime());
        assertEquals(LocalDateTime.of(2023, 9, 25, 9, 0), store2.getCloseTime());
    }

    @Test
    public void 매장_상세_조회_실패() {
        // Given
        Long nonExistentStoreId = 999L;
        // When
        when(storeRepository.findByStoreId(nonExistentStoreId))
                .thenReturn(Optional.empty()
                );

        // Then
        assertThrows(GlobalException.class,
                () -> storeService.detailsStore(nonExistentStoreId)
        );
    }
}
