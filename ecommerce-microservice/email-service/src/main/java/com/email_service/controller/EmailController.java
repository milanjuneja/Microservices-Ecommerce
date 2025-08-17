package com.email_service.controller;

import com.email_service.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send/email")
    public ResponseEntity<?> sendEmail(@RequestParam String email,
                                       @RequestParam String otp,
                                       @RequestParam String subject,
                                       @RequestParam String text) throws MessagingException {
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
