package com.payment_service.controller;

import com.payment_service.clients.OrderClient;
import com.payment_service.clients.SellerClient;
import com.payment_service.clients.TransactionClient;
import com.payment_service.clients.UserClient;
import com.payment_service.dto.*;
import com.payment_service.entity.PaymentOrder;
import com.payment_service.request.PaymentOrderRequest;
import com.payment_service.response.ApiResponse;
import com.payment_service.service.PaymentService;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserClient userClient;

    private final SellerClient sellerClient;
    private final OrderClient orderClient;
    private final TransactionClient transactionClient;

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(
            @PathVariable String paymentId,
            @RequestParam String paymentLinkId,
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);

        boolean paymentSuccess = paymentService.proceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);

        if (paymentSuccess) {
            for (Long orderId : paymentOrder.getOrders()) {
                OrderDTO orderById = orderClient.getOrderById(email, orderId);
                transactionClient.createTransaction(orderById);

                SellerDTO seller = sellerClient.getSellerById(orderById.getSellerId());
                SellerReportDTO report = sellerClient.getSellerReport(seller);
                report.setTotalOrders(report.getTotalOrders() + 1);
                report.setTotalEarnings(report.getTotalEarnings() + orderById.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + orderById.getOrderItems().size());
                sellerClient.updateSellerReport(report);

            }
        }
        return new ResponseEntity<>(new ApiResponse("Payment Success"), HttpStatus.CREATED);

    }

    @PostMapping("/create/order")
    public ResponseEntity<PaymentOrderDTO> createOrder(@RequestBody PaymentOrderRequest request){
        PaymentOrder order = paymentService.createOrder(request.getUser(), request.getOrders());
        return new ResponseEntity<>(PaymentOrderDTO.fromEntity(order), HttpStatus.OK);
    }
    @PostMapping(value = "/create/link", produces = "application/json")
    public ResponseEntity<PaymentLink> createLink(@RequestBody UserDTO user, @RequestParam Long amount, @RequestParam Long orderId) throws RazorpayException {
        return new ResponseEntity<>(paymentService.createRazorpayPaymentLink(user, amount, orderId), HttpStatus.CREATED);
    }
    @PostMapping("/save/payment/order")
    public ResponseEntity<PaymentOrderDTO> savePaymentOrder(@RequestBody PaymentOrderDTO paymentOrder){
        PaymentOrder paymentOrder1 = paymentService.savePaymentOrder(paymentOrder);
        return new ResponseEntity<>(PaymentOrderDTO.fromEntity(paymentOrder1), HttpStatus.OK);
    }
}