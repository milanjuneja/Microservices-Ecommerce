package com.order_service.service;

import com.order_service.dto.AddressDTO;
import com.order_service.dto.CartDTO;
import com.order_service.dto.OrderDTO;
import com.order_service.dto.UserDTO;
import com.order_service.entity.Order;
import com.order_service.entity.OrderItem;
import com.order_service.enums.OrderStatus;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<OrderDTO> createOrder(UserDTO user, AddressDTO address, CartDTO cart);
    Order findOrderById(Long id) throws Exception;
    List<Order> usersOrderHistory(Long userId);
    List<Order> sellersOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
    Order cancelOrder(Long orderId, Long userId) throws Exception;
    OrderItem getOrderItemById(Long id) throws Exception;


}