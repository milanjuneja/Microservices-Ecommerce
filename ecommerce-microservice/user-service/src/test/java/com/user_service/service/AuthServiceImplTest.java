package com.user_service.service;

import com.user_service.clients.CartClient;
import com.user_service.clients.SellerClient;
import com.user_service.domain.USER_ROLE;
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
import com.user_service.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private CartClient cartClient;
    @Mock private VerificationCodeRepository verificationCodeRepository;
    @Mock private SellerClient sellerClient;
    @Mock private OtpProducer otpProducer;

    @InjectMocks
    private AuthServiceImpl authService;

    private SignupRequest signupRequest;
    private LoginRequest loginRequest;
    private User user;
    private VerificationCode verificationCode;
    private SellerDTO sellerDTO;

    @BeforeEach
    void setup() {

        signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setOtp("123456");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setOtp("123456");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setPassword("encodedPassword");

        verificationCode = new VerificationCode();
        verificationCode.setEmail("john.doe@example.com");
        verificationCode.setOtp("123456");

        sellerDTO = new SellerDTO();
        sellerDTO.setEmail("seller@example.com");
        sellerDTO.setRole(USER_ROLE.ROLE_SELLER);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() throws Exception {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(null);
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);


        User created = authService.createUser(signupRequest);

        assertNotNull(created);
        assertEquals("john.doe@example.com", created.getEmail());
        verify(cartClient).createCart(any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_userAlreadyExists() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(new User());

        Exception ex = assertThrows(Exception.class, () -> authService.createUser(signupRequest));
        assertEquals("User Already exists", ex.getMessage());
    }

    @Test
    void sendLoginOtp_userNotFound_throwsException() {
        when(userRepository.findByEmail("signing_testuser@test.com")).thenReturn(null);

        Exception ex = assertThrows(Exception.class, 
            () -> authService.sendLoginOtp("signing_testuser@test.com", USER_ROLE.ROLE_CUSTOMER));
        assertEquals("user not exist with provided email", ex.getMessage());
    }

    @Test
    void signIn_userSuccess() throws Exception {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        VerificationCode code = new VerificationCode();
        code.setEmail("john.doe@example.com");
        code.setOtp("123456");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user);
        when(verificationCodeRepository.findByEmail("john.doe@example.com")).thenReturn(code);

        UserResponse res = authService.signIn(loginRequest);

        assertEquals("john.doe@example.com", res.getUsername());
        assertEquals("ROLE_CUSTOMER", res.getRoles());
    }

    @Test
    void signIn_wrongOtp_throwsException() {
        LoginRequest req = new LoginRequest("john.doe@example.com", "wrong");

        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setRole(com.user_service.domain.USER_ROLE.ROLE_CUSTOMER);

        VerificationCode code = new VerificationCode();
        code.setEmail("john.doe@example.com");
        code.setOtp("1234");

        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(user);
        when(verificationCodeRepository.findByEmail("john.doe@example.com")).thenReturn(code);

        Exception ex = assertThrows(Exception.class, () -> authService.signIn(req));
        assertEquals("Wrong OTP", ex.getMessage());
    }
    @Test
    void sendLoginOtp_shouldDeleteExistingVerificationCode() throws Exception {
        when(verificationCodeRepository.findByEmail("john.doe@example.com")).thenReturn(verificationCode);

        when(verificationCodeRepository.save(any(VerificationCode.class)))
                .thenReturn(verificationCode);

        authService.sendLoginOtp("john.doe@example.com", USER_ROLE.ROLE_CUSTOMER);

        verify(verificationCodeRepository).delete(verificationCode);
        verify(verificationCodeRepository).save(any(VerificationCode.class));
        verify(otpProducer).sendOtpEvent(any(OtpEmailEvent.class));
    }

    @Test
    void signIn_ForSeller_WhenSellerNotFound_ShouldThrowException() {
        // Arrange
        LoginRequest sellerLoginRequest = new LoginRequest();
        sellerLoginRequest.setEmail("seller_nonexistent@example.com");
        sellerLoginRequest.setOtp("123456");

        when(sellerClient.findSellerByEmail("nonexistent@example.com")).thenReturn(null);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            authService.signIn(sellerLoginRequest);
        });

        assertEquals("User not found", exception.getMessage());
        verify(sellerClient).findSellerByEmail("nonexistent@example.com");
    }
}