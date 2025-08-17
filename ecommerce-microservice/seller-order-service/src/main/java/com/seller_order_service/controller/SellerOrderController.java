package com.seller_order_service.controller;

import com.seller_order_service.clients.OrderClient;
import com.seller_order_service.clients.SellerClient;
import com.seller_order_service.domain.OrderStatus;
import com.seller_order_service.dto.OrderDTO;
import com.seller_order_service.dto.SellerDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller/orders")
@AllArgsConstructor
public class SellerOrderController {

    private final OrderClient orderClient;
    private final SellerClient sellerClient;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(@RequestHeader("X-User-Email") String email) throws Exception {
        SellerDTO seller = sellerClient.findSellerByEmail(email);
        return new ResponseEntity<>(orderClient.sellersOrder(seller.getId(), email), HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestHeader("X-User-Email") String email,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
            ) throws Exception {
        return new ResponseEntity<>(orderClient.updateOrderStatus(email, orderId, orderStatus), HttpStatus.OK);
    }


}