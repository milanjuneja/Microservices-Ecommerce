package com.seller_order_service.clients;

import com.seller_order_service.domain.OrderStatus;
import com.seller_order_service.dto.OrderDTO;
import com.seller_order_service.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderClient {
    @GetMapping("/products/api/{id}")
    ProductDTO getProductById(@PathVariable Long id);
    @GetMapping("/api/orders/get/sellers/orders")
    List<OrderDTO> sellersOrder(@RequestParam Long sellerId,
                                @RequestHeader("X-User-Email") String email);

    @PatchMapping("/api/orders/update/order/status")
    OrderDTO updateOrderStatus(@RequestHeader("X-User-Email") String email,
                               @RequestParam Long orderId,
                               @RequestParam OrderStatus orderStatus);
}