package com.payment_service.clients;

import com.payment_service.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "transaction-service")
public interface TransactionClient {
    @PostMapping("create/transaction")
    void createTransaction(OrderDTO order);
}
