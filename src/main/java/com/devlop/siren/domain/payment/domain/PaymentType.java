package com.devlop.siren.domain.payment.domain;

import lombok.Getter;

@Getter
public enum PaymentType {
    CARD("카드"),
    CASH("현금");

    private final String paymentName;

    PaymentType(String paymentName) {
        this.paymentName = paymentName;
    }
}
