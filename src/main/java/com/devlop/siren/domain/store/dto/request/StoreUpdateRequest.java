package com.devlop.siren.domain.store.dto.request;

import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreUpdateRequest {
  private String storeName;
  private String storePhone;
  private String city;
  private String street;
  private Integer zipCode;
  private LocalTime openTime;
  private LocalTime closeTime;

  public StoreUpdateRequest(
      String storeName,
      String storePhone,
      String city,
      String street,
      Integer zipCode,
      LocalTime openTime,
      LocalTime closeTime) {
    this.storeName = storeName;
    this.storePhone = storePhone;
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
    this.openTime = openTime;
    this.closeTime = closeTime;
  }
}
