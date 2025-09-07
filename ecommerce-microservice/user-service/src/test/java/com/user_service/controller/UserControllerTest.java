package com.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_service.domain.USER_ROLE;
import com.user_service.entity.User;
import com.user_service.entity.VerificationCode;
import com.user_service.repo.VerificationCodeRepository;
import com.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        UserService userService()  {
            return Mockito.mock(UserService.class);
        }
        @Bean
        VerificationCodeRepository verificationCodeRepository(){
            return Mockito.mock(VerificationCodeRepository.class);
        }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;


    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private VerificationCode verificationCode;

    @BeforeEach
    void setup() {


        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setPassword("encodedPassword");

        verificationCode = new VerificationCode();
        verificationCode.setId(1L);
        verificationCode.setEmail("john.doe@example.com");
        verificationCode.setOtp("123456");

        MockitoAnnotations.openMocks(this);
    }
    @Test
    void createUserHandler() throws Exception{
        Mockito.when(userService.findUserByEmail("john.doe@example.com")).thenReturn(user);
        mockMvc.perform(get("/user/users/profile")
                .header("X-User-Email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getUserFromToken() throws Exception{
        Mockito.when(userService.findUserByEmail("john.doe@example.com")).thenReturn(user);
        mockMvc.perform(get("/user/api/users/profile")
                .header("X-User-Email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getSellerEmailFromToken() throws Exception {
        mockMvc.perform(get("/user/api/seller/profile")
                        .header("X-User-Email", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("john.doe@example.com"));
    }

    @Test
    void verifyEmail_success() throws Exception {
        Mockito.when(verificationCodeRepository.findByOtp("123456")).thenReturn(verificationCode);
        mockMvc.perform(patch("/user/seller/verify/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.otp").value("123456"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
    @Test
    void verifyEmail_failure() throws Exception {
        Mockito.when(verificationCodeRepository.findByOtp("123456")).thenReturn(null);
        mockMvc.perform(patch("/user/seller/verify/123456"))
                .andExpect(result -> assertThrows(Exception.class,
                        () -> userService.findUserByEmail("notfound@example.com")))

                .andExpect(result ->
                        assertEquals("Invalid OTP", result.getResolvedException().getMessage()));

    }

    @Test
    void saveSellerCode() throws Exception{
        Mockito.when(verificationCodeRepository.save(any(VerificationCode.class))).thenReturn(verificationCode);
        mockMvc.perform(post("/user/seller/save/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationCode)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.otp").value("123456"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

    }

    @Test
    void saveUser() throws Exception{
        mockMvc.perform(post("/user/save/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        Mockito.verify(userService).saveUser(any(User.class));
    }

    @Test
    void findByEmail() throws Exception{
        Mockito.when(userService.findUserByEmail("john.doe@example.com")).thenReturn(user);

        mockMvc.perform(get("/user/find/email/john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
}
