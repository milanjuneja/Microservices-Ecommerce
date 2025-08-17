package com.user_service.controller;

import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import com.user_service.request.LoginOtpRequest;
import com.user_service.request.LoginRequest;
import com.user_service.request.SignupRequest;
import com.user_service.response.ApiResponse;
import com.user_service.response.AuthResponse;
import com.user_service.response.UserResponse;
import com.user_service.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {

        User user = authService.createUser(req);
        AuthResponse response = new AuthResponse();
        response.setMessage("Register Success");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody LoginOtpRequest req) throws Exception {

        authService.sendLoginOtp(req.getEmail(), req.getRole());
        ApiResponse response = new ApiResponse();

        response.setMessage("Otp sent successfully");


        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/signing/verify")
    public ResponseEntity<UserResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {

        UserResponse user = authService.signIn(req);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
}