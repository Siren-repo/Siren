package com.devlop.siren.store.service;

import com.devlop.siren.store.domain.Store;
import com.devlop.siren.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.store.repository.StoreRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;


public class StoreServiceTest {

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

        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest
                ("Updated Store Name","Updated Store Phone"
                        ,"Updated City","Updated Street",12345,
                        LocalDateTime.of(2023, 9, 25, 9, 0),
                        LocalDateTime.of(2023, 9, 25, 18, 0));


        when(storeRepository.findByStoreId(1L)).thenReturn(Optional.of(mockStore));

        System.out.println("변경 전 매장 이름 > > > : " + mockStore.getStoreName());
        System.out.println("변경 전  매장 번호 > > > : " +mockStore.getStorePhone());
        System.out.println("변경 전 매장 도시 > > > : " +mockStore.getCity());
        System.out.println("변경 전 매장 도로명 > > > : " +mockStore.getStreet());
        System.out.println("변경 전 매장 우편번호 > > > : " +mockStore.getZipCode());
        System.out.println("변경 전 매장 오픈시간 > > > : " +mockStore.getOpenTime());
        System.out.println("변경 전 매장 마감시간 > > > : " +mockStore.getCloseTime());

        System.out.println("-----------------------------------------------");

        storeService.updateStore(1L, storeUpdateRequest);


        // 변경된 mockStore 객체를 콘솔에 출력
        System.out.println("변경된 매장 이름 > > > : " + mockStore.getStoreName());
        System.out.println("변경된 매장 번호 > > > : " +mockStore.getStorePhone());
        System.out.println("변경된 매장 도시 > > > : " +mockStore.getCity());
        System.out.println("변경된 매장 도로명 > > > : " +mockStore.getStreet());
        System.out.println("변경된 매장 우편번호 > > > : " +mockStore.getZipCode());
        System.out.println("변경된 매장 오픈시간 > > > : " +mockStore.getOpenTime());
        System.out.println("변경된 매장 마감시간 > > > : " +mockStore.getCloseTime());

        // passed or FalLure
        verify(mockStore, times(1)).update(storeUpdateRequest);
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

        // 예외가 발생해야 함
        storeService.updateStore(storeId, storeUpdateRequest);
    }
    @Test
    public void 부분_매장_업데이트(){

        StoreUpdateRequest storeUpdateRequest = new StoreUpdateRequest
                ("Updated Store Name", null, null, null, null, null, null);


        when(storeRepository.findByStoreId(1L)).thenReturn(Optional.of(mockStore));

        System.out.println("변경 전 매장 이름 > > > : " + mockStore.getStoreName());
        System.out.println("변경 전  매장 번호 > > > : " +mockStore.getStorePhone());
        System.out.println("변경 전 매장 도시 > > > : " +mockStore.getCity());
        System.out.println("변경 전 매장 도로명 > > > : " +mockStore.getStreet());
        System.out.println("변경 전 매장 우편번호 > > > : " +mockStore.getZipCode());
        System.out.println("변경 전 매장 오픈시간 > > > : " +mockStore.getOpenTime());
        System.out.println("변경 전 매장 마감시간 > > > : " +mockStore.getCloseTime());

        System.out.println("-----------------------------------------------");

        storeService.updateStore(1L, storeUpdateRequest);



        // 변경된 mockStore 객체를 콘솔에 출력
        System.out.println("변경된 매장 이름 > > > : " + mockStore.getStoreName());
        System.out.println("변경된 매장 번호 > > > : " +mockStore.getStorePhone());
        System.out.println("변경된 매장 도시 > > > : " +mockStore.getCity());
        System.out.println("변경된 매장 도로명 > > > : " +mockStore.getStreet());
        System.out.println("변경된 매장 우편번호 > > > : " +mockStore.getZipCode());
        System.out.println("변경된 매장 오픈시간 > > > : " +mockStore.getOpenTime());
        System.out.println("변경된 매장 마감시간 > > > : " +mockStore.getCloseTime());
        // passed or FalLure
        verify(mockStore, times(1)).update(storeUpdateRequest);
    }
}
