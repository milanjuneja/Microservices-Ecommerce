package com.payment_service.entity;

import com.payment_service.domain.PaymentMethod;
import com.payment_service.domain.PaymentOrderStatus;
import com.payment_service.dto.OrderDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus paymentOrderStatus = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;
    private String paymentLinkId;

    private Long userId;

    private Set<Long> orders = new HashSet<>();
}
