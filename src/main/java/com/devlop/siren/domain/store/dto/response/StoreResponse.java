package com.devlop.siren.domain.store.dto.response;

import com.devlop.siren.domain.store.domain.Store;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StoreResponse {
    private String storeName;
    private String storePhone;
    private String city;
    private String street;
    private Integer zipCode;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;


    public static StoreResponse from(Store store){
        return StoreResponse.builder()
                .storeName(store.getStoreName())
                .storePhone(store.getStorePhone())
                .city(store.getCity())
                .street(store.getStreet())
                .zipCode(store.getZipCode())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .build();
    }
}
