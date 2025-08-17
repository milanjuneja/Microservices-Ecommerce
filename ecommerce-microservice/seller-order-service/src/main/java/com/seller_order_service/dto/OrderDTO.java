package com.seller_order_service.dto;

import com.seller_order_service.domain.OrderStatus;
import com.seller_order_service.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderId;
    private Long userId;
    private Long sellerId;
    private List<OrderItemDTO> orderItems; // You'll need to define OrderItemDTO as well
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