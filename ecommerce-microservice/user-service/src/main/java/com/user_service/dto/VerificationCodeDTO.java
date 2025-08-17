package com.user_service.dto;

import com.user_service.entity.VerificationCode;
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

    public static VerificationCodeDTO from(VerificationCode code) {
        VerificationCodeDTO dto = new VerificationCodeDTO();
        dto.setId(code.getId());
        dto.setOtp(code.getOtp());
        dto.setEmail(code.getEmail());
        dto.setUserId(code.getUserId());
        dto.setSellerId(code.getSellerId());
        return dto;
    }

    public static VerificationCode toEntity(VerificationCodeDTO dto) {
        VerificationCode code = new VerificationCode();
        code.setId(dto.getId());
        code.setOtp(dto.getOtp());
        code.setEmail(dto.getEmail());
        code.setUserId(dto.getUserId());
        code.setSellerId(dto.getSellerId());
        return code;
    }
}