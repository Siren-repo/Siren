package com.devlop.siren.store.dto;

import com.devlop.siren.store.domain.Store;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRegisterDto {
    private String storeName;
    private String storePhone;
    private String city;
    private String street;
    private Integer zipCode;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;

    public static Store from(StoreRegisterDto registerDto,Double lat,Double lon){
        return Store.builder()
                .storeName(registerDto.storeName)
                .storePhone(registerDto.storePhone)
                .city(registerDto.city)
                .street(registerDto.street)
                .zipCode(registerDto.zipCode)
                .openTime(registerDto.openTime)
                .closeTime(registerDto.closeTime)
                .latitude(lat)
                .longitude(lon)
                .build();
    }
}
