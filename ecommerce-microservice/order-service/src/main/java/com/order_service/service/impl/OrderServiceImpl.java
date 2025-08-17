package com.order_service.service.impl;

import com.order_service.clients.ProductClient;
import com.order_service.clients.SellerClient;
import com.order_service.dto.*;
import com.order_service.enums.OrderStatus;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.enums.PaymentStatus;
import com.order_service.kafka.OtpEmailEvent;
import com.order_service.kafka.OtpProducer;
import com.order_service.repo.OrderItemRepository;
import com.order_service.repo.OrderRepository;
import com.order_service.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final SellerClient sellerClient;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final OtpProducer otpProducer;

    @Override
    public Set<OrderDTO> createOrder(UserDTO user, AddressDTO shippingAddress, CartDTO cart) {

        if (!user.getAddress().contains(shippingAddress)) {
            user.getAddress().add(shippingAddress);
        }
        AddressDTO address = sellerClient.saveAddress(shippingAddress);
        Map<Long, List<CartItemDTO>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> {
                    ProductDTO product = productClient.getProductById(item.getProductId());
                    return product.getSellerId();
                }));
        Set<OrderDTO> orders = new HashSet<>();

        for (Map.Entry<Long, List<CartItemDTO>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItemDTO> items = entry.getValue();

            int totalOrderPrices = items.stream().mapToInt(CartItemDTO::getSellingPrice).sum();
            int totalItems = items.stream().mapToInt(CartItemDTO::getQuantity).sum();
            Order createdOrder = new Order();
            createdOrder.setUserId(user.getId());
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrices);
            createdOrder.setTotalSellingPrice(totalOrderPrices);
            createdOrder.setTotalItem(totalItems);
            createdOrder.setShipmentAddressId(address.getId());
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.setPaymentStatus(PaymentStatus.PENDING);

            Order savedOrder = orderRepository.save(createdOrder);
            OrderDTO orderDTO = OrderDTO.fromEntity(savedOrder);
            orders.add(orderDTO);

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItemDTO item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProductId(item.getProductId());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                savedOrder.getOrderItems().add(orderItem);
                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
                String subject = "Order placed";
                String text = "your order with order items - " + item.getProductId() + " has been place";
                OtpEmailEvent event = new OtpEmailEvent(user.getEmail(), String.valueOf(item.getProductId()), subject, text);
                otpProducer.sendOtpEvent(event);
            }

        }
        return orders;
    }

    @Override
    public Order findOrderById(Long id) throws Exception {
        return orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return orderRepository.findBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, Long userId) throws Exception {
        Order order = findOrderById(orderId);

        if (!userId.equals(order.getUserId()))
            throw new Exception("you don't have access to this order");

        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public OrderItem getOrderItemById(Long id) throws Exception {
        return orderItemRepository.findById(id).orElseThrow(() ->
                new Exception("Order item dost not exist"));
    }
}