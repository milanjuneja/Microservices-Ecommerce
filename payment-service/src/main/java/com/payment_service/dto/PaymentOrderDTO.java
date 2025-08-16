package com.payment_service.dto;

import com.payment_service.domain.PaymentMethod;
import com.payment_service.domain.PaymentOrderStatus;
import com.payment_service.entity.PaymentOrder;
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

    public static PaymentOrderDTO fromEntity(PaymentOrder entity) {
        PaymentOrderDTO dto = new PaymentOrderDTO();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentOrderStatus(entity.getPaymentOrderStatus());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setPaymentLinkId(entity.getPaymentLinkId());
        dto.setUserId(entity.getUserId());
        dto.setOrders(entity.getOrders());
        return dto;
    }

    public static PaymentOrder toEntity(PaymentOrderDTO dto) {
        PaymentOrder entity = new PaymentOrder();
        entity.setId(dto.getId());
        entity.setAmount(dto.getAmount());
        entity.setPaymentOrderStatus(dto.getPaymentOrderStatus());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setPaymentLinkId(dto.getPaymentLinkId());
        entity.setUserId(dto.getUserId());
        entity.setOrders(dto.getOrders());
        return entity;
    }
}