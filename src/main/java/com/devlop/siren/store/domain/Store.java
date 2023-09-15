package com.devlop.siren.store.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;
    @Column(name = "storeName")
    private String storeName;
    @Column(name = "storePhone")
    private String storePhone;
    @Column(name = "city",columnDefinition = "NVARCHAR(255) NOT NULL" )
    private String city;
    @Column(name = "street",columnDefinition = "NVARCHAR(255) NOT NULL" )
    private String street;
    @Column(name = "zipCode")
    private Integer zipCode;
    @Column(name = "openTime")
    private LocalDateTime openTime;
    @Column(name = "closeTime")
    private LocalDateTime closeTime;
    @Column(name = "latitude",columnDefinition = "NVARCHAR(255) NOT NULL" )
    private Double latitude;
    @Column(name = "longitude", columnDefinition = "NVARCHAR(255) NOT NULL" )
    private Double longitude;
}
