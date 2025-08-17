package com.order_service.dto;

import com.order_service.enums.PaymentMethod;
import com.order_service.enums.PaymentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderDTO {

    private Long id;
    private Long amount;
    private PaymentOrderStatus paymentOrderStatus = PaymentOrderStatus.PENDING;
    private PaymentMethod paymentMethod;
    private String paymentLinkId;
    private Long userId;
    private Set<Long> orders = new HashSet<>();
}
