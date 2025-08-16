package com.user_service.clients;

import com.user_service.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


// Cart Service
@FeignClient(name = "cart-service")
public interface CartClient {
    @PostMapping("/api/cart/create")
    CartDTO createCart(@RequestBody CartDTO cartDTO);
}

