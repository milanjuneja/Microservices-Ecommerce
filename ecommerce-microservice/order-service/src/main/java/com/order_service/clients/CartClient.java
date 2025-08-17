package com.order_service.clients;

import com.order_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "cart-service")
    public interface CartClient {
        @GetMapping("/api/cart")
        CartDTO findUserCart(@RequestHeader("X-User-Email") String email);
}