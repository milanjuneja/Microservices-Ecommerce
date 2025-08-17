package com.cart_service.service;

import com.cart_service.dto.CartDTO;
import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItems;

public interface CartService {

    CartItems addCartItem(
            Long userId,
            Long productId,
            String size,
            int quantity
    );

    Cart findUserCart(Long userId);

    Cart createCart(Cart cart);

    Cart saveCart(CartDTO cart);
}