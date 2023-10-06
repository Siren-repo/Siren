package com.devlop.siren.store.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreUpdateRequest {
    private String storeName;
    private String storePhone;
    private String city;
    private String street;
    private Integer zipCode;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    public StoreUpdateRequest(String storeName, String storePhone, String city, String street,
                              Integer zipCode, LocalDateTime openTime, LocalDateTime closeTime) {
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
