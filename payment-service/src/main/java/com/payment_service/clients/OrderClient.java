package com.payment_service.clients;

import com.payment_service.domain.OrderStatus;
import com.payment_service.dto.OrderDTO;
import com.payment_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderClient {

    @GetMapping("/api/orders/{orderId}")
    OrderDTO getOrderById(@RequestHeader("X-User-Email") String email, @PathVariable Long orderId);

    @PostMapping("")
    void saveOrder(OrderDTO orderById);
}