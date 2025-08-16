package com.order_service.clients;

import com.order_service.dto.AddressDTO;
import com.order_service.dto.SellerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seller-service")
public interface SellerClient {
    @GetMapping("/sellers/find/email")
    SellerDTO findSellerByEmail(@RequestParam("email") String email);

    @PostMapping("/sellers/save/address")
    AddressDTO saveAddress(@RequestBody AddressDTO shippingAddress);
}