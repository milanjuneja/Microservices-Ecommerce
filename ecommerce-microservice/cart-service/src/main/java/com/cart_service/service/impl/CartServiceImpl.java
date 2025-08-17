package com.cart_service.service.impl;

import com.cart_service.clients.ProductClient;
import com.cart_service.dto.CartDTO;
import com.cart_service.dto.ProductDTO;
import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItems;
import com.cart_service.repo.CartItemRepository;
import com.cart_service.repo.CartRepository;
import com.cart_service.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductClient productClient;

    @Override
    public CartItems addCartItem(Long userId, Long productId, String size, int quantity) {
        Cart cart = findUserCart(userId);
        CartItems isPresent = cartItemRepository.findByCartAndProductIdAndSize(cart, productId, size);
        if(isPresent == null){
            CartItems cartItems = new CartItems();
            cartItems.setProductId(productId);
            cartItems.setQuantity(quantity);
            cartItems.setUserId(userId);
            cartItems.setSize(size);
            ProductDTO productById = productClient.getProductById(productId);
            cartItems.setMrpPrice(quantity * productById.getMrpPrice());
            cart.getCartItems().add(cartItems);
            cartItems.setCart(cart);


            int totalPrice = quantity * productById.getSellingPrice();
            cartItems.setSellingPrice(totalPrice);
            return cartItemRepository.save(cartItems);
        }
        return isPresent;
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItems cartItem: cart.getCartItems()) {
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
            cartItem.setProductId(cartItem.getProductId());
        }
        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItems(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));
        return cart;
    }

    @Override
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart saveCart(CartDTO cart) {
        Cart entity = CartDTO.toEntity(cart);
        return cartRepository.save(entity);
    }

    private int calculateDiscountPercentage(double mrpPrice, double sellingPrice){
        if(mrpPrice <= 0)
            return 0;
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount/mrpPrice) * 100;
        return (int) discountPercentage;
    }
}