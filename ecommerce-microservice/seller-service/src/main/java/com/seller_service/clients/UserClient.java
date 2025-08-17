package com.seller_service.clients;

import com.seller_service.dto.VerificationCodeDTO;
import com.seller_service.request.LoginRequest;
import com.seller_service.response.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user/api/seller/profile")
    String findSellerEmailByJwtToken(@RequestHeader("Authorization") String jwt);

    @PostMapping("/auth/signing/verify")
    AuthResponse signIn(@RequestBody LoginRequest req);

    @PatchMapping("/user/seller/verify/{otp}")
    VerificationCodeDTO verifySellerOtp(@PathVariable("otp") String otp);

    @PostMapping("/user/seller/save/code")
    void saveSellerCode(@RequestBody VerificationCodeDTO verificationCodeDTO);
    @PostMapping("/user/seller/send/email")
    void sendEmail(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text
    );
}