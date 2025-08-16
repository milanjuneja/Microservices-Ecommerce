package com.user_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "email-service")
public interface EmailClient {

    @PostMapping("/send/email")
    void sendVerificationOtpEmail(@RequestParam String email,
                                  @RequestParam String otp,
                                  @RequestParam String subject,
                                  @RequestParam String text);
}
