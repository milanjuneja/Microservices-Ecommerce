package com.order_service.dto;

import com.order_service.entity.Order;
import com.order_service.enums.OrderStatus;
import com.order_service.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private String orderId;
    private Long userId;
    private Long sellerId;
    private List<OrderItemDTO> orderItems = new ArrayList<>();
    private Long shipmentAddressId;
    private Long paymentId;
    private double totalMrpPrice;
    private Integer totalSellingPrice;
    private Integer discount;
    private OrderStatus orderStatus;
    private int totalItem;
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private LocalDateTime orderDate = LocalDateTime.now();
    private LocalDateTime deliverDate = orderDate.plusDays(7);

    // Convert DTO to Entity
    public Order toEntity() {
        Order order = new Order();
        order.setId(this.id);
        order.setOrderId(this.orderId);
        order.setUserId(this.userId);
        order.setSellerId(this.sellerId);
        order.setShipmentAddressId(this.shipmentAddressId);
        order.setPaymentId(this.paymentId);
        order.setTotalMrpPrice(this.totalMrpPrice);
        order.setTotalSellingPrice(this.totalSellingPrice);
        order.setDiscount(this.discount);
        order.setOrderStatus(this.orderStatus);
        order.setTotalItem(this.totalItem);
        order.setPaymentStatus(this.paymentStatus);
        order.setOrderDate(this.orderDate);
        order.setDeliverDate(this.deliverDate);

        if (this.orderItems != null) {
            order.setOrderItems(this.orderItems.stream()
                    .map(OrderItemDTO::toEntity)
                    .collect(Collectors.toList()));
        }

        return order;
    }

    // Convert Entity to DTO
    public static OrderDTO fromEntity(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setSellerId(order.getSellerId());
        dto.setShipmentAddressId(order.getShipmentAddressId());
        dto.setPaymentId(order.getPaymentId());
        dto.setTotalMrpPrice(order.getTotalMrpPrice());
        dto.setTotalSellingPrice(order.getTotalSellingPrice());
        dto.setDiscount(order.getDiscount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalItem(order.getTotalItem());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliverDate(order.getDeliverDate());

        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream()
                    .map(OrderItemDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}