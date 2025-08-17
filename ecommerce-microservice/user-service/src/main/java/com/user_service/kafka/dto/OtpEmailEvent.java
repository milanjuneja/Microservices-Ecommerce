package com.user_service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpEmailEvent {
    private String email;
    private String otp;
    private String subject;
    private String text;
}
