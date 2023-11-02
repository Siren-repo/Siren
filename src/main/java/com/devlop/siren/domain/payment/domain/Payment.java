package com.devlop.siren.domain.payment.domain;

import com.devlop.siren.domain.payment.dto.response.PaymentResponse;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/*
 * 성공 orderId, paymentKey, amount 3개를 리턴 받아야 함.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId; // 결제 id
    @Column(name = "amount")
    private Long amount; // 결제 금액
    @Column(name="order_name")
    private String orderName; // 주문상품
    @Column(name = "order_id")
    private String orderId; // 상품 ID (랜덤 String )

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType; // 현금  or 카드


    @Column(name = "payment_key")
    private String paymentKey; // 결제 승인시 발급 해주는 키

    @Column(name ="payment_status", columnDefinition = "TINYINT(1)")
    private Boolean paymentStatus; // 결제 승인 성공 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Payment(Long paymentId, Long amount, String orderName,Boolean paymentStatus,
                   String orderId, PaymentType paymentType, String paymentKey,
                   User user, Store store) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.orderName = orderName;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.paymentKey = paymentKey;
        this.user = user;
        this.store = store;
    }

    public PaymentResponse toEntity(){
        return PaymentResponse.builder()
                .paymentType(paymentType.getPaymentName())
                .amount(amount)
                .orderName(orderName)
                .orderId(orderId)
                .userNickName(user.getNickName())
                .build();
    }
}
