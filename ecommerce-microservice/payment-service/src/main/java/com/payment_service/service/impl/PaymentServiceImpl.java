package com.payment_service.service.impl;

import com.payment_service.clients.OrderClient;
import com.payment_service.domain.PaymentOrderStatus;
import com.payment_service.domain.PaymentStatus;
import com.payment_service.dto.OrderDTO;
import com.payment_service.dto.PaymentOrderDTO;
import com.payment_service.dto.UserDTO;
import com.payment_service.entity.PaymentOrder;
import com.payment_service.repo.PaymentOrderRepository;
import com.payment_service.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderClient orderClient;

    private final String API_KEY = "rzp_test_iRxURRAqSx7u4y";
    private final String API_SECRET_KEY = "x1RwsrNRovtKlFhQNuFqjDXP";

    private final String STRIPE_SECRET_KEY = "stripe";


    @Override
    public PaymentOrder createOrder(UserDTO user, Set<OrderDTO> orders) {

        Long amount = orders.stream()
                .mapToLong(OrderDTO::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        Set<Long> collect = orders.stream()
                .map(OrderDTO::getId).collect(Collectors.toSet());
        paymentOrder.setOrders(collect);
        paymentOrder.setUserId(user.getId());

        return paymentOrderRepository.save(paymentOrder);

    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
        return paymentOrderRepository.findById(orderId).orElseThrow(() ->
                new Exception("Payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
        PaymentOrder byPaymentLinkId = paymentOrderRepository.findByPaymentLinkId(orderId);
        if(byPaymentLinkId == null)
            throw new Exception("Payment order not found with provided payment link id -> " + orderId);
        return byPaymentLinkId;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)){
            RazorpayClient razorPay = new RazorpayClient(API_KEY, API_SECRET_KEY);
            Payment payment = razorPay.payments.fetch(paymentId);
            String status = payment.get("status");
            String jwt = "";
            if(status.equals("captured")){
                Set<Long> orders = paymentOrder.getOrders();
                for (Long order: orders) {
                    OrderDTO orderById = orderClient.getOrderById(jwt, order);
                    orderById.setPaymentStatus(PaymentStatus.COMPLETED);
                    orderClient.saveOrder(orderById);
                }
                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.FAILED);
            paymentOrderRepository.save(paymentOrder);
        }

        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserDTO user, Long amount, Long orderId) throws RazorpayException {
        amount = amount * 100;
        try {
            RazorpayClient razorPay = new RazorpayClient(API_KEY, API_SECRET_KEY);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFirstName() + " " + user.getLastName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:5173/payment-success/"+orderId);
            paymentLinkRequest.put("callback_method", "get");

            return razorPay.paymentLink.create(paymentLinkRequest);
//            String paymentLinkUrl = paymentLink.get("short_url");
//            String paymentLinkId = paymentLink.get("id");
        }
        catch (Exception e){
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(UserDTO user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = STRIPE_SECRET_KEY;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel/")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("inr")
                                .setUnitAmount(amount*100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Milan Payment")
                                                .build()
                                ).build()
                        ).build()
                )
                .build();
        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    public PaymentOrder savePaymentOrder(PaymentOrderDTO paymentOrder) {
        PaymentOrder entity = PaymentOrderDTO.toEntity(paymentOrder);
        return paymentOrderRepository.save(entity);
    }
}