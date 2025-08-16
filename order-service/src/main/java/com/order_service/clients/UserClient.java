package com.order_service.clients;

import com.order_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user/api/users/profile")
    UserDTO findUserByEmail(@RequestHeader("X-User-Email") String email);
}