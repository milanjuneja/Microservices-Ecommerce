package com.seller_service.service.impl;

import com.seller_service.clients.UserClient;
import com.seller_service.entity.Address;
import com.seller_service.entity.Seller;
import com.seller_service.exceptions.SellerException;
import com.seller_service.model.AccountStatus;
import com.seller_service.model.USER_ROLE;
import com.seller_service.repo.AddressRepository;
import com.seller_service.repo.SellerRepository;
import com.seller_service.service.SellerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

//    private final PasswordEncoder passwordEncoder;
    private final UserClient userClient;
    private final AddressRepository addressRepository;

    @Override
    public Seller getSellerProfileFromJwt(String jwt) throws Exception {
        String email = userClient.findSellerEmailByJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist != null)
            throw new Exception("Seller already exist, use different email");

        Address address = addressRepository.save(seller.getPickUpAddress());
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(seller.getPassword());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickUpAddress(address);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);

    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new SellerException("Seller not found with id - " + id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null)
            throw new Exception("Seller not found");
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller = this.getSellerById(id);

        if (seller.getSellerName() != null)
            existingSeller.setSellerName(seller.getSellerName());
        if (seller.getMobile() != null)
            existingSeller.setMobile(seller.getMobile());
        if (seller.getEmail() != null)
            existingSeller.setEmail(seller.getEmail());
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null)
            existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountNumber() != null) {
            existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }
        if (seller.getPickUpAddress() != null
                && seller.getPickUpAddress().getAddress() != null
                && seller.getPickUpAddress().getMobile() != null
                && seller.getPickUpAddress().getCity() != null
                && seller.getPickUpAddress().getState() != null
                && seller.getPickUpAddress().getPinCode() != null) {
            existingSeller.getPickUpAddress().setAddress(seller.getPickUpAddress().getAddress());
            existingSeller.getPickUpAddress().setMobile(seller.getPickUpAddress().getMobile());
            existingSeller.getPickUpAddress().setCity(seller.getPickUpAddress().getCity());
            existingSeller.getPickUpAddress().setState(seller.getPickUpAddress().getState());
            existingSeller.getPickUpAddress().setPinCode(seller.getPickUpAddress().getPinCode());
        }

        if (seller.getGSTIN() != null)
            existingSeller.setGSTIN(seller.getGSTIN());

        return sellerRepository.save(existingSeller);


    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = this.getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }

    @Override
    public Address saveAddress(Address address) throws Exception {
        return addressRepository.save(address);
    }
}