package com.devlop.siren.domain.payment.dto.request;

import com.devlop.siren.domain.payment.domain.Payment;
import com.devlop.siren.domain.payment.domain.PaymentType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentRequest {
    @NotNull
    private PaymentType paymentType; // 카드 , 현금
    @NotNull
    private Long amount; // 금액
    @NotNull
    private String orderName; // 주문상품

    private String successUrl; // 성공시 url
    private String failUrl; // 실패시 url

    public Payment toEntity() {
        return Payment.builder()
                .paymentType(paymentType)
                .amount(amount)
                .orderName(orderName)
                .orderId(UUID.randomUUID().toString())
                .paymentStatus(false)
                .build();
    }
}
