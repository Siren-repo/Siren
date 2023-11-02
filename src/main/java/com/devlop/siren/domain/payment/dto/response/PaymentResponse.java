package com.devlop.siren.domain.payment.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentResponse {

    private String paymentType;
    private Long amount;
    private String orderName;
    private String orderId;
    private String successUrl;
    private String failUrl;
    private String userNickName;

    @Builder
    public PaymentResponse(String paymentType, Long amount, String orderName,
                           String orderId, String successUrl, String failUrl, String userNickName) {
        this.paymentType = paymentType;
        this.amount = amount;
        this.orderName = orderName;
        this.orderId = orderId;
        this.successUrl = successUrl;
        this.failUrl = failUrl;
        this.userNickName = userNickName;
    }
}
