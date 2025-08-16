package com.seller_service.controller;

import com.seller_service.clients.UserClient;
import com.seller_service.dto.AddressDTO;
import com.seller_service.dto.SellerDTO;
import com.seller_service.dto.VerificationCodeDTO;
import com.seller_service.entity.Address;
import com.seller_service.entity.Seller;
import com.seller_service.entity.SellerReport;
import com.seller_service.exceptions.SellerException;
import com.seller_service.model.AccountStatus;
import com.seller_service.request.LoginRequest;
import com.seller_service.response.AuthResponse;
import com.seller_service.service.SellerReportService;
import com.seller_service.service.SellerService;
import com.seller_service.util.OtpUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
@AllArgsConstructor
public class SellerController {
    
    private final UserClient userClient;

    private final SellerService sellerService;

    private SellerReportService sellerReportService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        req.setEmail("seller_" + req.getEmail());
        AuthResponse authResponse = userClient.signIn(req);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCodeDTO verificationCodeDTO = userClient.verifySellerOtp(otp);
        if (verificationCodeDTO == null || !verificationCodeDTO.getOtp().equals(otp)) {
            throw new Exception("Wrong otp....");
        }
        return new ResponseEntity<>(sellerService.verifyEmail(verificationCodeDTO.getEmail(), otp), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception{
        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
        VerificationCodeDTO verificationCode = new VerificationCodeDTO();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCode.setSellerId(seller.getId());
        userClient.saveSellerCode(verificationCode);
        //verificationCodeRepository.save(verificationCode);

        String subject = "Email verification Code";
        String text = "verify your account using this link ";
        String frontEndUrl = "http://localhost:3000/verify-seller/";
        userClient.sendEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontEndUrl);
//        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontEndUrl);
        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        return new ResponseEntity<>(sellerService.getSellerById(id), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("X-User-Email") String email) throws Exception {
        return new ResponseEntity<>(sellerService.getSellerByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("X-User-Email") String email) throws Exception {
        Seller seller = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(sellerReportService.getSellerReport(seller), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required = false) AccountStatus status){
        return new ResponseEntity<>(sellerService.getAllSellers(status), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader("X-User-Email") String email,
                                               @RequestBody Seller seller) throws Exception {
        Seller profile = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(sellerService.updateSeller(profile.getId(), seller), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/find/email")
    public ResponseEntity<SellerDTO> findSellerByEmail(@RequestParam("email") String email) throws Exception {
        Seller sellerByEmail = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(SellerDTO.from(sellerByEmail), HttpStatus.OK);
    }

    @PostMapping("/save/address")
    public ResponseEntity<AddressDTO> saveAddress(@RequestBody AddressDTO addressDTO) throws Exception {
        Address address = sellerService.saveAddress(AddressDTO.to(addressDTO));
        return new ResponseEntity<>(AddressDTO.from(address), HttpStatus.OK);
    }

    @GetMapping("/profile/order")
    public ResponseEntity<SellerDTO> getSellerByJwtOrder(@RequestHeader("X-User-Email") String email) throws Exception {
        Seller sellerProfileFromJwt = sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(SellerDTO.from(sellerProfileFromJwt), HttpStatus.OK);

    }

}