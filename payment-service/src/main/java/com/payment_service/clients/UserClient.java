package com.payment_service.clients;

import com.payment_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user/api/users/profile")
    UserDTO findUserByEmail(@RequestHeader("X-User-Email") String email);

    @PostMapping("/user/save/user")
    void saveUser(@RequestBody UserDTO userDto);
}