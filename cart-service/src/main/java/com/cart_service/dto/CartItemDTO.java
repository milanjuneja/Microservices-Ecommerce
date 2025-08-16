package com.cart_service.dto;

import com.cart_service.entity.CartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId; // Assumes you have a ProductDTO class
    private String size;
    private int quantity;
    private Integer mrpPrice;
    private Integer sellingPrice;
    private Long userId;

    public static CartItemDTO from(CartItems cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProductId());
        dto.setSize(cartItem.getSize());
        dto.setQuantity(cartItem.getQuantity());
        dto.setMrpPrice(cartItem.getMrpPrice());
        dto.setSellingPrice(cartItem.getSellingPrice());
        dto.setUserId(cartItem.getUserId());
        return dto;
    }
    public static CartItems toEntity(CartItemDTO dto) {
        CartItems cartItem = new CartItems();
        cartItem.setId(dto.getId());
        cartItem.setProductId(dto.getProductId());
        cartItem.setSize(dto.getSize());
        cartItem.setQuantity(dto.getQuantity());
        cartItem.setMrpPrice(dto.getMrpPrice());
        cartItem.setSellingPrice(dto.getSellingPrice());
        cartItem.setUserId(dto.getUserId());
        return cartItem;
    }
}