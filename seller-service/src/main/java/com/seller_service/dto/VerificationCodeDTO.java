package com.seller_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCodeDTO {

    private Long id;

    private String otp;
    private String email;

    private Long userId;
    private Long sellerId;
}
