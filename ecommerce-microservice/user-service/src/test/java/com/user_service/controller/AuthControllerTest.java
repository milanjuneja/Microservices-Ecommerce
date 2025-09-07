package com.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import com.user_service.request.LoginOtpRequest;
import com.user_service.request.LoginRequest;
import com.user_service.request.SignupRequest;
import com.user_service.response.UserResponse;
import com.user_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        AuthService authService()  {
            return Mockito.mock(AuthService.class);
        }
    }
    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;
    private SignupRequest signupRequest;
    private User user;

    private LoginRequest loginRequest;
    private LoginOtpRequest loginOtpRequest;

    private UserResponse userResponse;

    @BeforeEach
    void setup() {

        userResponse = new UserResponse();
        userResponse.setUsername("john.doe@example.com");
        userResponse.setRoles(USER_ROLE.ROLE_CUSTOMER.toString());

        loginOtpRequest = new LoginOtpRequest();
        loginOtpRequest.setEmail("john.doe@example.com");
        loginOtpRequest.setOtp("123456");
        loginOtpRequest.setRole(USER_ROLE.ROLE_CUSTOMER);

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setPassword("encodedPassword");

        signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setOtp("123456");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setOtp("123456");

        MockitoAnnotations.openMocks(this);
    }
    @Test
    void signup_shouldReturn201Created() throws Exception {

        Mockito.when(authService.createUser(any(SignupRequest.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Register Success"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));
    }

    @Test
    void sendOtp_shouldReturn200Ok() throws Exception {

        Mockito.doNothing().when(authService).sendLoginOtp(eq("john@example.com"), eq(USER_ROLE.ROLE_CUSTOMER));

        mockMvc.perform(post("/auth/send/login-signup-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginOtpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Otp sent successfully"));
    }

    @Test
    void login_shouldReturn200OkWithUser() throws Exception {

        Mockito.when(authService.signIn(any(LoginRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/auth/signing/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe@example.com"))
                .andExpect(jsonPath("$.roles").value("ROLE_CUSTOMER"));
    }

    @Test
    void login_shouldReturn401UnauthorizedWhenInvalid() throws Exception {

        Mockito.when(authService.signIn(any(LoginRequest.class))).thenReturn(null);

        mockMvc.perform(post("/auth/signing/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
