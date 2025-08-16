package com.order_service.controller;

import com.order_service.clients.CartClient;
import com.order_service.clients.PaymentClient;
import com.order_service.clients.SellerClient;
import com.order_service.clients.UserClient;
import com.order_service.dto.*;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.enums.OrderStatus;
import com.order_service.enums.PaymentMethod;
import com.order_service.request.PaymentOrderRequest;
import com.order_service.response.PaymentLinkResponse;
import com.order_service.service.OrderService;
import com.razorpay.PaymentLink;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserClient userClient;
    private final CartClient cartClient;
    private final SellerClient sellerClient;
    private final PaymentClient paymentClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrder(
            @RequestHeader("X-User-Email") String email,
            @RequestBody AddressDTO shippingAddress,
            @RequestParam PaymentMethod paymentMethod
            ) throws Exception {

        UserDTO user = userClient.findUserByEmail(email);
        CartDTO cart = cartClient.findUserCart(email);
        Set<OrderDTO> orders = orderService.createOrder(user, shippingAddress, cart);
        PaymentOrderRequest request = new PaymentOrderRequest();
        request.setOrders(orders);
        request.setUser(user);
        PaymentOrderDTO paymentOrder = paymentClient.createOrder(request);

        PaymentLinkResponse res = new PaymentLinkResponse();
        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink paymentLink = paymentClient.createRazorpayPaymentLink(
                user, paymentOrder.getAmount(), paymentOrder.getId()
            );

            String paymentUrl = paymentLink.get("short_url");
            String paymentUrlId = paymentLink.get("id");
            res.setPayment_link_id(paymentUrl);
            paymentOrder.setPaymentLinkId(paymentUrlId);
            paymentClient.savePaymentOrder(paymentOrder);

        }
        else {
            String paymentUrl = paymentClient.createStripePaymentLink(user,
                    paymentOrder.getAmount(),
                    paymentOrder.getId());

            res.setPayment_link_url(paymentUrl);
        }
        PaymentLinkResponse response = new PaymentLinkResponse();
        response.setPayment_link_id("jjg");
        response.setPayment_link_url("gkkg");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        return new ResponseEntity<>(orderService.usersOrderHistory(user.getId()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId,
                                                 @RequestHeader("X-User-Email") String email) throws Exception {
        userClient.findUserByEmail(email);
        Order orderById = orderService.findOrderById(orderId);
        return new ResponseEntity<>(OrderDTO.fromEntity(orderById), HttpStatus.OK);
    }

    @GetMapping("item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId,
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        userClient.findUserByEmail(email);
        return new ResponseEntity<>(orderService.getOrderItemById(orderItemId), HttpStatus.ACCEPTED);

    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(
        @PathVariable Long orderId,
        @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        Order order = orderService.cancelOrder(orderId, user.getId());

//        SellerDTO seller = sellerClient.findSellerByEmail(order.getSellerId());
//        SellerReport report = sellerReportService.getSellerReport(seller);
//
//        report.setCanceledOrders(report.getCanceledOrders()+1);
//        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
//        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/get/sellers/orders")
    public ResponseEntity<List<OrderDTO>> getSellersOrders(
            @PathVariable Long SellerId,
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        userClient.findUserByEmail(email);
        List<OrderDTO> list = new ArrayList<>();
        List<Order> orders = orderService.sellersOrder(SellerId);
        for (Order order: orders) {
            list.add(OrderDTO.fromEntity(order));
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PatchMapping("/update/order/status")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestHeader("X-User-Email") String email,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
    ) throws Exception {
        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>(OrderDTO.fromEntity(order), HttpStatus.OK);
    }

}