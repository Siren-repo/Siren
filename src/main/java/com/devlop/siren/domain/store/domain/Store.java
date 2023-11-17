package com.devlop.siren.domain.store.domain;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.store.dto.request.StoreUpdateRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "store_phone")
    private String storePhone;
    @Column(name = "city", columnDefinition = "NVARCHAR(255) NOT NULL")
    private String city;
    @Column(name = "street", columnDefinition = "NVARCHAR(255) NOT NULL")
    private String street;


    @Column(name = "zip_code")
    private Integer zipCode;
    @Column(name = "open_time")
    private LocalDateTime openTime;
    @Column(name = "close_time")
    private LocalDateTime closeTime;
    @Column(name = "latitude", columnDefinition = "NVARCHAR(255) NOT NULL")
    private Double latitude;
    @Column(name = "longitude", columnDefinition = "NVARCHAR(255) NOT NULL")
    private Double longitude;
    @OneToMany(mappedBy = "store")
    private List<Order> orders = new ArrayList<Order>();

    @Builder
    public Store(Long storeId, String storeName, String storePhone, String city, String street,
                 Integer zipCode, LocalDateTime openTime, LocalDateTime closeTime, Double latitude, Double longitude) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storePhone = storePhone;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(StoreUpdateRequest storeUpdateRequest) {
        this.storeName = Objects.requireNonNullElse(storeUpdateRequest.getStoreName(), this.storeName);
        this.storePhone = Objects.requireNonNullElse(storeUpdateRequest.getStorePhone(), this.storePhone);
        this.city = Objects.requireNonNullElse(storeUpdateRequest.getCity(), this.city);
        this.street = Objects.requireNonNullElse(storeUpdateRequest.getStreet(), this.street);
        this.zipCode = Objects.requireNonNullElse(storeUpdateRequest.getZipCode(), this.zipCode);
        this.openTime = Objects.requireNonNullElse(storeUpdateRequest.getOpenTime(), this.openTime);
        this.closeTime = Objects.requireNonNullElse(storeUpdateRequest.getCloseTime(), this.closeTime);
    }
}
