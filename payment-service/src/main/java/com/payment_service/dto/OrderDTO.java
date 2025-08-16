package com.payment_service.dto;

import com.payment_service.domain.OrderStatus;
import com.payment_service.domain.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderId;
    private Long userId;
    private Long sellerId;
    private Set<Long> orderItems;
    private Long shipmentAddressId;
    private Long paymentId;
    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer discount;
    private OrderStatus orderStatus;
    private int totalItem;
    private PaymentStatus paymentStatus;
    private LocalDateTime orderDate;
    private LocalDateTime deliverDate;
}