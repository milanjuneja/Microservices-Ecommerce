package com.user_service.request;

import com.user_service.domain.USER_ROLE;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginOtpRequest {
    private String email;
    private String otp;
    private USER_ROLE role;
}