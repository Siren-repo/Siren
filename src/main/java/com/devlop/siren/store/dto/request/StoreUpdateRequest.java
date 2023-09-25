package com.devlop.siren.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StoreUpdateRequest {
    private String storeName;
    private String storePhone;
    private String city;
    private String street;
    private Integer zipCode;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
}
