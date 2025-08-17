package com.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Long id;
    private Long userId; // assuming you already have a UserDTO
    private Set<CartItemDTO> cartItems; // assuming you already have a CartItemDTO
    private double totalSellingPrice;
    private int totalItems;
    private double totalMrpPrice;
    private int discount;
    private String couponCode;
}