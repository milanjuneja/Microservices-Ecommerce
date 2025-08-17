package com.payment_service.service;

import com.payment_service.dto.OrderDTO;
import com.payment_service.dto.PaymentOrderDTO;
import com.payment_service.dto.UserDTO;
import com.payment_service.entity.PaymentOrder;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {

    PaymentOrder createOrder(UserDTO user, Set<OrderDTO> orders);
    PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;

    PaymentLink createRazorpayPaymentLink(UserDTO user, Long amount, Long orderId) throws RazorpayException;
    String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException;

    PaymentOrder savePaymentOrder(PaymentOrderDTO paymentOrder);
}