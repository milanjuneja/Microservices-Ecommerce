package com.coupon_service.clients;

import com.coupon_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cart-service")
public interface CartClient {
    @GetMapping("/api/cart")
    CartDTO findUserCart(@RequestHeader("X-User-Email") String email);

    @PostMapping("/api/cart/save/cart")
    CartDTO saveCart(@RequestHeader("X-User-Email") String email, @RequestBody CartDTO cart);
}