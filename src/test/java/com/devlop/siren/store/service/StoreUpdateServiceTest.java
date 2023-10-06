package com.devlop.siren.store.service;

import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.store.domain.Store;
import com.devlop.siren.store.request.StoreUpdateRequest;
import com.devlop.siren.store.repository.StoreRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class StoreUpdateServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private Store mockStore;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockStore = Store.builder()
                .storeId(1L)
                .storeName("First Store Name")
                .storePhone("First Store Phone")
                .city("First Store City")
                .street("First Store Street")
                .zipCode(54321)
                .openTime( LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime( LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();

    }

    @Test
    public void 정상_업데이트_테스트() {
        //given
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest
                ("Updated Store Name","Updated Store Phone"
                        ,"Updated City","Updated Street",12345,
                        LocalDateTime.of(2023, 9, 25, 9, 0),
                        LocalDateTime.of(2023, 9, 25, 18, 0));

        //when
        when(storeRepository.findByStoreId(1L)).thenReturn(Optional.of(mockStore));

        storeService.updateStore(1L, storeUpdateRequest);
        //then

        Assert.assertEquals("Updated Store Name", mockStore.getStoreName());
        Assert.assertEquals("Updated Store Phone", mockStore.getStorePhone());
        Assert.assertEquals("Updated City", mockStore.getCity());
        Assert.assertEquals("Updated Street", mockStore.getStreet());
        Assert.assertEquals(Integer.valueOf("12345"), mockStore.getZipCode());
//
//        verify(mockStore, times(1)).update(storeUpdateRequest);
    }

    @Test
    public void 존재하지않는_매장_업데이트_테스트() {
        Long storeId = 2L;
        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest
                ("Updated Store Name","Updated Store Phone"
                        ,"Updated City","Updated Street",12345,
                        LocalDateTime.of(2023, 9, 25, 9, 0),
                        LocalDateTime.of(2023, 9, 25, 18, 0));


        when(storeRepository.findByStoreId(storeId)).thenReturn(Optional.empty());

//        // 예외가 발생해야 함
//        storeService.updateStore(storeId, storeUpdateRequest);

        assertThrows(GlobalException.class,
                () -> storeService.updateStore(storeId, storeUpdateRequest)
        );

    }
    @Test
    public void 부분_매장_업데이트(){
        //given

        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest
                ("Updated Store Name", null, null, null, null, null, null);

        //when
        when(storeRepository.findByStoreId(1L)).thenReturn(Optional.of(mockStore));

        storeService.updateStore(1L, storeUpdateRequest);

        //then
        Assert.assertEquals("Updated Store Name", mockStore.getStoreName());
        Assert.assertEquals("First Store Phone", mockStore.getStorePhone());
        Assert.assertEquals("First Store City", mockStore.getCity());
        Assert.assertEquals("First Store Street", mockStore.getStreet());
        Assert.assertEquals(Integer.valueOf("54321"), mockStore.getZipCode());

        //verify(mockStore, times(1)).update(storeUpdateRequest);
    }



}
