package com.payment_service.request;

import com.payment_service.dto.OrderDTO;
import com.payment_service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrderRequest {
    private UserDTO user;
    private Set<OrderDTO> orders;
}