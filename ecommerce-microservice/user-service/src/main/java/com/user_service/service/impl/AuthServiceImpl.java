package com.user_service.service.impl;

import com.user_service.clients.CartClient;
import com.user_service.clients.EmailClient;
import com.user_service.clients.SellerClient;
import com.user_service.domain.USER_ROLE;
import com.user_service.dto.CartDTO;
import com.user_service.dto.SellerDTO;
import com.user_service.entity.User;
import com.user_service.entity.VerificationCode;
import com.user_service.kafka.dto.OtpEmailEvent;
import com.user_service.kafka.producer.OtpProducer;
import com.user_service.repo.UserRepository;
import com.user_service.repo.VerificationCodeRepository;
import com.user_service.request.LoginRequest;
import com.user_service.request.SignupRequest;
import com.user_service.response.UserResponse;
import com.user_service.service.AuthService;
import com.user_service.utility.OtpUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartClient cartClient;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailClient emailClient;
    private final SellerClient sellerClient;
    private final OtpProducer otpProducer;
    @Override
    public User createUser(SignupRequest request) throws Exception {

        User user = userRepository.findByEmail(request.getEmail());
        if(user != null)
            throw new Exception("User Already exists");

        User createdUser = new User();
        createdUser.setFirstName(request.getFirstName());
        createdUser.setLastName(request.getLastName());
        createdUser.setEmail(request.getEmail());
        createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
        createdUser.setMobile("9890890890");
        createdUser.setPassword(passwordEncoder.encode(request.getOtp()));

        user = userRepository.save(createdUser);
        CartDTO cart = new CartDTO();
        cart.setUserId(user.getId());
        cartClient.createCart(cart);
        //cartRepository.save(cart);
        return user;
    }

    @Override
    public void sendLoginOtp(String email, USER_ROLE role) throws Exception {
        String SIGNING_PREFIX = "signing_";

        if(email.startsWith(SIGNING_PREFIX)){
           email = email.substring(SIGNING_PREFIX.length());

           if(role.equals(USER_ROLE.ROLE_SELLER)){
               //Seller seller = sellerRepository.findByEmail(email);
               //(seller == null)
                  // throw new Exception("seller not exist with provided email");

           }else{
               User user = userRepository.findByEmail(email);
               if(user == null){
                   throw new Exception("user not exist with provided email");
               }

           }

        }
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if(isExist != null){
            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);

        String subject = "Login/signup otp";
        String text = "your login/signup otp is - " + otp;
        OtpEmailEvent event = new OtpEmailEvent(email, otp, subject, text);
        otpProducer.sendOtpEvent(event);
        //emailClient.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public UserResponse signIn(LoginRequest request) throws Exception {
        String username = request.getEmail();
        String otp = request.getOtp();
        UserResponse res = new UserResponse();

        if (username.startsWith("seller")) {
            username = username.substring(7);

            SellerDTO sellerByEmail = sellerClient.findSellerByEmail(username);
            if(sellerByEmail == null)
                throw new Exception("User not found");
            VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
            if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
                throw new Exception("Wrong OTP");
            }
            res.setRoles(sellerByEmail.getRole().toString());
            res.setUsername(sellerByEmail.getEmail());
        }else{
            User user = userRepository.findByEmail(username);
            if (user == null) {
                throw new Exception("User not found");
            }
            VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
            if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
                throw new Exception("Wrong OTP");
            }
            res.setRoles(user.getRole().toString());
            res.setUsername(user.getEmail());
        }
        return res;

    }
}