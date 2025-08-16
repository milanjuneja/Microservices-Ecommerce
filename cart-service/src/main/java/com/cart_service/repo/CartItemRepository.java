package com.cart_service.repo;

import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItems, Long> {
    CartItems findByCartAndProductIdAndSize(Cart cart, Long productId, String size);
}