package com.seller_service.dto;

import com.seller_service.entity.Address;
import com.seller_service.entity.Seller;
import com.seller_service.model.AccountStatus;
import com.seller_service.model.BankDetails;
import com.seller_service.model.BusinessDetails;
import com.seller_service.model.USER_ROLE;
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

    private BusinessDetails businessDetails;
    private BankDetails bankDetails;
    private Address pickUpAddress;

    private String GSTIN;
    private USER_ROLE role;
    private boolean isEmailVerified;
    private AccountStatus accountStatus;

    public static SellerDTO from(Seller seller) {
        return SellerDTO.builder()
                .id(seller.getId())
                .sellerName(seller.getSellerName())
                .mobile(seller.getMobile())
                .email(seller.getEmail())
                .password(seller.getPassword())
                .businessDetails(seller.getBusinessDetails())
                .bankDetails(seller.getBankDetails())
                .pickUpAddress(seller.getPickUpAddress())
                .GSTIN(seller.getGSTIN())
                .role(seller.getRole())
                .isEmailVerified(seller.isEmailVerified())
                .accountStatus(seller.getAccountStatus())
                .build();
    }

    public Seller toSeller() {
        Seller seller = new Seller();
        seller.setId(this.id);
        seller.setSellerName(this.sellerName);
        seller.setMobile(this.mobile);
        seller.setEmail(this.email);
        seller.setPassword(this.password);
        seller.setBusinessDetails(this.businessDetails);
        seller.setBankDetails(this.bankDetails);
        seller.setPickUpAddress(this.pickUpAddress);
        seller.setGSTIN(this.GSTIN);
        seller.setRole(this.role);
        seller.setEmailVerified(this.isEmailVerified);
        seller.setAccountStatus(this.accountStatus);
        return seller;
    }
}