package com.cart_service.dto;

import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

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

    public static Cart toEntity(CartDTO dto) {
        Cart cart = new Cart();
        cart.setId(dto.getId());
        cart.setUserId(dto.getUserId());
        cart.setTotalSellingPrice(dto.getTotalSellingPrice());
        cart.setTotalItems(dto.getTotalItems());
        cart.setTotalMrpPrice(dto.getTotalMrpPrice());
        cart.setDiscount(dto.getDiscount());
        cart.setCouponCode(dto.getCouponCode());
        return cart;
    }
    public static CartDTO from(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setTotalSellingPrice(cart.getTotalSellingPrice());
        dto.setTotalItems(cart.getTotalItems());
        dto.setTotalMrpPrice(cart.getTotalMrpPrice());
        dto.setDiscount(cart.getDiscount());
        dto.setCouponCode(cart.getCouponCode());

        // Convert CartItem to CartItemDTO
//        Set<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
//                .map(CartItemDTO::from)
//                .collect(Collectors.toSet());
//
//        dto.setCartItems(cartItemDTOs);
        return dto;
    }
}