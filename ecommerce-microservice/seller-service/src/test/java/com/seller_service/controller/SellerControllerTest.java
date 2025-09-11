package com.seller_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seller_service.clients.UserClient;
import com.seller_service.dto.VerificationCodeDTO;
import com.seller_service.entity.Seller;
import com.seller_service.entity.SellerReport;
import com.seller_service.model.AccountStatus;
import com.seller_service.model.USER_ROLE;
import com.seller_service.request.LoginRequest;
import com.seller_service.response.AuthResponse;
import com.seller_service.service.SellerReportService;
import com.seller_service.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SellerController.class)
class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SellerReportService sellerReportService;
    @MockitoBean
    private SellerService sellerService;

    @MockitoBean
    private UserClient userClient;
    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private AuthResponse authResponse;
    private VerificationCodeDTO verificationCodeDTO;
    private Seller seller;
    private SellerReport sellerReport;
    @BeforeEach
    void setup(){

        seller = new Seller();
        seller.setId(1L);
        seller.setEmail("milan.juneja97@gmail.com");

        sellerReport = new SellerReport();
        sellerReport.setSeller(seller);
        sellerReport.setId(1L);
        sellerReport.setTotalOrders(20);

        verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setSellerId(1L);
        verificationCodeDTO.setOtp("123456");
        verificationCodeDTO.setEmail("milan.juneja97@gmail.com");

        authResponse = new AuthResponse();
        authResponse.setJwt("jwt");
        authResponse.setRole(USER_ROLE.ROLE_SELLER);
        authResponse.setMessage("Logged in");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("milan.juneja97@gmail.com");
        loginRequest.setOtp("123456");
    }
    @Test
    void loginSeller() throws Exception {
        Mockito.when(userClient.signIn(any(LoginRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/sellers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwt"));
    }

    @Test
    void verifySellerEmail_success() throws Exception{
        Mockito.when(userClient.verifySellerOtp("123456"))
                .thenReturn(verificationCodeDTO);
        Mockito.when(sellerService.verifyEmail(verificationCodeDTO.getEmail(), "123456"))
                .thenReturn(seller);

        mockMvc.perform(patch("/sellers/verify/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("milan.juneja97@gmail.com"));
    }

    @Test
    void verifySellerEmail_failure() throws Exception{
        Mockito.when(userClient.verifySellerOtp("123456")).thenReturn(null);

        mockMvc.perform(patch("/sellers/verify/123456"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> {
                    Exception ex = result.getResolvedException();
                    assertNotNull(ex);
                    assertEquals("Wrong otp....", ex.getMessage());
                });
    }

    @Test
    void createSeller() throws Exception {
        Mockito.when(sellerService.createSeller(any(Seller.class))).thenReturn(seller);
        mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(seller)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("milan.juneja97@gmail.com"));
    }

    @Test
    void getSellerById() throws Exception{
        Mockito.when(sellerService.getSellerById(1L)).thenReturn(seller);
        mockMvc.perform(get("/sellers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("milan.juneja97@gmail.com"));
    }

    @Test
    void getSellerByJwt() throws Exception{
        Mockito.when(sellerService.getSellerByEmail(seller.getEmail())).thenReturn(seller);
        mockMvc.perform(get("/sellers/profile")
                        .header("X-User-Email", "milan.juneja97@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("milan.juneja97@gmail.com"));

    }

    @Test
    void getSellerReport() throws Exception{
        Mockito.when(sellerService.getSellerByEmail(seller.getEmail())).thenReturn(seller);
        Mockito.when(sellerReportService.getSellerReport(any(Seller.class))).thenReturn(sellerReport);
        mockMvc.perform(get("/sellers/report")
                        .header("X-User-Email", "milan.juneja97@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getAllSeller() throws Exception{
        Mockito.when(sellerService.getAllSellers(AccountStatus.PENDING_VERIFICATION)).thenReturn(List.of(seller));
        mockMvc.perform(get("/sellers").param("status", AccountStatus.PENDING_VERIFICATION.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("milan.juneja97@gmail.com"));

    }

    @Test
    void updateSeller() throws Exception{
        Seller updated = new Seller();
        updated.setEmail("aman@gmail.com");
        updated.setId(2L);

        Mockito.when(sellerService.getSellerByEmail(seller.getEmail())).thenReturn(seller);
        Mockito.when(sellerService.updateSeller(seller.getId(), updated)).thenReturn(updated);
        mockMvc.perform(patch("/sellers")
                        .header("X-User-Email", "milan.juneja97@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("aman@gmail.com"));
    }

    @Test
    void deleteSeller() throws Exception{
        mockMvc.perform(delete("/sellers/1"))
                .andExpect(status().isNoContent());
        Mockito.verify(sellerService).deleteSeller(1L);

    }

    @Test
    void findSellerByEmail() {
    }

    @Test
    void saveAddress() {
    }

    @Test
    void getSellerByJwtOrder() {
    }
}