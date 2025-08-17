package com.cart_service.service.impl;

import com.cart_service.clients.ProductClient;
import com.cart_service.dto.ProductDTO;
import com.cart_service.entity.CartItems;
import com.cart_service.repo.CartItemRepository;
import com.cart_service.service.CartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    @Override
    public CartItems updateCartItem(Long userId, Long cartItemId, CartItems cartItems) throws Exception {
        CartItems item = findCartItemById(cartItemId);
        Long userId1 = item.getCart().getUserId();
        if(userId1.equals(userId)){
            ProductDTO productById = productClient.getProductById(item.getProductId());
            item.setQuantity(cartItems.getQuantity());
            item.setMrpPrice(item.getQuantity() * productById.getMrpPrice());
            item.setSellingPrice(item.getQuantity() * productById.getSellingPrice());
            return cartItemRepository.save(item);
        }
        throw new Exception("You can't update this cart item");
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {
        CartItems item = findCartItemById(cartItemId);
        Long userId1 = item.getCart().getUserId();
        if(userId1.equals(userId)){
            cartItemRepository.delete(item);
        }else{
            throw new Exception("You can't delete this cart item");
        }
    }

    @Override
    public CartItems findCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id).orElseThrow(() -> new Exception("Cart item not found with id -> " + id));
    }
}
