package com.seller_service.service;

import com.seller_service.dto.AddressDTO;
import com.seller_service.entity.Address;
import com.seller_service.entity.Seller;
import com.seller_service.exceptions.SellerException;
import com.seller_service.model.AccountStatus;

import java.util.List;

public interface SellerService {

    Seller getSellerProfileFromJwt(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status);

    Seller updateSeller(Long id, Seller seller) throws Exception;
    void deleteSeller(Long id) throws Exception;
    Seller verifyEmail(String email, String otp) throws Exception;

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;


    Address saveAddress(Address address) throws Exception;
}