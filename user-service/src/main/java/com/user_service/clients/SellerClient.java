package com.user_service.clients;

import com.user_service.dto.SellerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Seller Service
@FeignClient(name = "seller-service")
public interface SellerClient {
    @PostMapping("/sellers/find/email")
    SellerDTO findSellerByEmail(@RequestParam("email") String email);
}