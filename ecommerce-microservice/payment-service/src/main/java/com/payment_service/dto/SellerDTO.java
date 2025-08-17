package com.payment_service.dto;

import com.payment_service.domain.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerDTO {

    private Long id;
    private String sellerName;
    private String mobile;
    private String email;
    private String password;

    private String GSTIN;
    private USER_ROLE role;
    private boolean isEmailVerified;
}