package com.devlop.siren.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private String user;
    private Long itemId;
    private Long quantity;
}
