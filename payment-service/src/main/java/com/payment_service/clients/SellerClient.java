package com.payment_service.clients;

import com.payment_service.dto.AddressDTO;
import com.payment_service.dto.SellerDTO;
import com.payment_service.dto.SellerReportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seller-service")
public interface SellerClient {
    @GetMapping("/sellers/find/id")
    SellerDTO getSellerById(@RequestParam("sellerId") Long sellerId);

    @PostMapping("/sellers/save/address")
    AddressDTO saveAddress(@RequestBody AddressDTO shippingAddress);

    @GetMapping("/sellers/get/report")
    SellerReportDTO getSellerReport(SellerDTO seller);

    @PostMapping("/sellers/update/report")
    void updateSellerReport(SellerReportDTO report);
}