package com.cart_service.controller;

import com.cart_service.clients.ProductClient;
import com.cart_service.clients.UserClient;
import com.cart_service.dto.CartDTO;
import com.cart_service.dto.ProductDTO;
import com.cart_service.dto.UserDTO;
import com.cart_service.entity.Cart;
import com.cart_service.entity.CartItems;
import com.cart_service.request.AddItemRequest;
import com.cart_service.response.ApiResponse;
import com.cart_service.service.CartItemService;
import com.cart_service.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;

    private final UserClient userClient;
    private final ProductClient productClient;

    @GetMapping
    public ResponseEntity<Cart> findUserCart(@RequestHeader("X-User-Email") String email) throws Exception {

        UserDTO user = userClient.findUserByEmail(email);
        return new ResponseEntity<>(cartService.findUserCart(user.getId()), HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestHeader("X-User-Email") String email,
                                                     @RequestBody AddItemRequest req) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        ProductDTO product = productClient.getProductById(req.getProductId());
        cartService.addCartItem(user.getId(), product.getId(), req.getSize(), req.getQuantity());
        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart successfully");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(
            @RequestHeader("X-User-Email") String email,
            @PathVariable Long cartItemId
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse response = new ApiResponse();
        response.setMessage("Item removed from cart successfully");

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItems> updateCartItem(
        @PathVariable Long cartItemId,
        @RequestBody CartItems cartItems,
        @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        CartItems updatedCartItem = null;
        if(cartItems.getQuantity() > 0){
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItems);
        }

        return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);

    }

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestBody CartDTO cartDTO) throws Exception {
        return new ResponseEntity<>(cartService.createCart(CartDTO.toEntity(cartDTO)), HttpStatus.OK);
    }

    @PostMapping("/save/cart")
    public ResponseEntity<CartDTO> saveCart(@RequestHeader("X-User-Email") String email, @RequestBody CartDTO cart){
        Cart cart1 = cartService.saveCart(cart);
        return new ResponseEntity<>(CartDTO.from(cart1), HttpStatus.OK);
    }

}