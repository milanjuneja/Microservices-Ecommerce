package com.order_service.clients;

import com.order_service.dto.CartDTO;
import com.order_service.dto.OrderDTO;
import com.order_service.dto.PaymentOrderDTO;
import com.order_service.dto.UserDTO;
import com.order_service.entity.Order;
import com.order_service.request.PaymentOrderRequest;
import com.razorpay.PaymentLink;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/api/payment/create/order")
    PaymentOrderDTO createOrder(@RequestBody PaymentOrderRequest request);

    @PostMapping("/api/payment/create/link")
    PaymentLink createRazorpayPaymentLink(@RequestBody UserDTO user, @RequestParam Long amount, @RequestParam Long orderId);

    @PostMapping("/api/payment/save/payment/order")
    void savePaymentOrder(PaymentOrderDTO paymentOrder);

    @PostMapping("gkg/gg")
    String createStripePaymentLink(@RequestBody UserDTO user, @RequestParam Long amount, @RequestParam Long id);
}
