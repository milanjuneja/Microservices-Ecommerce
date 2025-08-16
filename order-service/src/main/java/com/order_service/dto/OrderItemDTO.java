package com.order_service.dto;

import com.order_service.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String size;
    private int quantity;
    private Integer mrpPrice;
    private Integer sellingPrice;
    private Long userId;
    // Convert DTO to Entity
    public OrderItem toEntity() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(this.id);
        orderItem.setProductId(this.productId);
        orderItem.setSize(this.size);
        orderItem.setQuantity(this.quantity);
        orderItem.setMrpPrice(this.mrpPrice);
        orderItem.setSellingPrice(this.sellingPrice);
        orderItem.setUserId(this.userId);
        return orderItem;
    }

    // Convert Entity to DTO
    public static OrderItemDTO fromEntity(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProductId());
        dto.setSize(orderItem.getSize());
        dto.setQuantity(orderItem.getQuantity());
        dto.setMrpPrice(orderItem.getMrpPrice());
        dto.setSellingPrice(orderItem.getSellingPrice());
        dto.setUserId(orderItem.getUserId());
        return dto;
    }
}