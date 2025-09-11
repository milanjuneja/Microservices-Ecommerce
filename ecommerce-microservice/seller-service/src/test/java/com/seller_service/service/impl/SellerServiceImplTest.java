package com.seller_service.service.impl;

import com.seller_service.clients.UserClient;
import com.seller_service.entity.Address;
import com.seller_service.entity.Seller;
import com.seller_service.model.AccountStatus;
import com.seller_service.repo.AddressRepository;
import com.seller_service.repo.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class SellerServiceImplTest {

    @Mock private SellerRepository sellerRepository;
    @Mock private UserClient userClient;
    @Mock private AddressRepository addressRepository;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private Seller seller;
    private Address address;

    @BeforeEach
    void start(){
        address = new Address();
        address.setId(1L);
        address.setAddress("Test Address");
        address.setCity("Test City");
        address.setState("TS");
        address.setPinCode("123456");
        address.setMobile("9999999999");

        seller = new Seller();
        seller.setId(1L);
        seller.setEmail("milan.juneja97@gmail.com");
        seller.setPassword("password");
        seller.setSellerName("Test Seller");
        seller.setPickUpAddress(address);

        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getSellerProfileFromJwt_success() throws Exception{
        Mockito.when(userClient.findSellerEmailByJwtToken("jwt")).thenReturn("milan.juneja97@gmail.com");
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(seller);

        Seller result = sellerService.getSellerProfileFromJwt("jwt");
        assertNotNull(result);
        assertEquals("milan.juneja97@gmail.com", result.getEmail());
        verify(userClient).findSellerEmailByJwtToken("jwt");

    }

    @Test
    void getSellerProfileFromJwt_failure() throws Exception{
        Mockito.when(userClient.findSellerEmailByJwtToken("jwt")).thenReturn(null);
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(null);

        Exception exception = assertThrows(Exception.class,
                () -> sellerService.getSellerProfileFromJwt("jwt"));

        assertTrue(exception.getMessage().contains("Seller not found"));
        verify(userClient).findSellerEmailByJwtToken("jwt");

    }

    @Test
    void createSeller_success() throws Exception{
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(null);
        Mockito.when(addressRepository.save(any(Address.class))).thenReturn(address);
        Mockito.when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        Seller result = sellerService.createSeller(seller);
        assertNotNull(result);
        assertEquals("milan.juneja97@gmail.com", result.getEmail());
        verify(sellerRepository).save(any(Seller.class));
    }

    @Test
    void createSeller_failure(){
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(seller);

        Exception exception = assertThrows(Exception.class, () -> sellerService.createSeller(seller));
        assertEquals("Seller already exist, use different email", exception.getMessage());
    }

    @Test
    void getSellerById_success() throws Exception{
        Mockito.when(sellerRepository.findById(1L)).thenReturn(Optional.ofNullable(seller));
        Seller sellerById = sellerService.getSellerById(1L);
        assertNotNull(sellerById);
        assertEquals("milan.juneja97@gmail.com", sellerById.getEmail());

    }
    @Test
    void getSellerById_failure() {
        Mockito.when(sellerRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(Exception.class, () -> sellerService.getSellerById(2L));
        assertEquals("Seller not found with id - " + 2L, exception.getMessage());
    }

    @Test
    void getSellerByEmail_success() throws Exception{
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(seller);
        Seller result = sellerService.getSellerByEmail("milan.juneja97@gmail.com");
        assertNotNull(result);
        assertEquals("milan.juneja97@gmail.com", result.getEmail());

    }

    @Test
    void getSellerByEmail_failure(){
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(null);
        Exception exception = assertThrows(Exception.class, () -> sellerService.getSellerByEmail("milan.juneja97@gmail"));
        assertEquals("Seller not found", exception.getMessage());

    }

    @Test
    void getAllSellers() {
        Mockito.when(sellerRepository.findByAccountStatus(AccountStatus.PENDING_VERIFICATION)).thenReturn(List.of(seller));
        List<Seller> result = sellerService.getAllSellers(AccountStatus.PENDING_VERIFICATION);
        assertNotNull(result);
        assertEquals(seller, result.get(0));
    }

    @Test
    void updateSeller() throws Exception{
        Seller update = new Seller();
        update.setSellerName("Updated Seller");
        update.setMobile("8888888888");
        Mockito.when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        Mockito.when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        Seller result = sellerService.updateSeller(1L, update);
        assertNotNull(result);
        assertEquals("Updated Seller", result.getSellerName());
        assertEquals("8888888888", result.getMobile());

    }

    @Test
    void deleteSeller_success() throws Exception {
        Mockito.when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        sellerService.deleteSeller(1L);
        verify(sellerRepository).delete(any(Seller.class));

    }

    @Test
    void deleteSeller_failure() {
        Mockito.when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        Exception exception = assertThrows(Exception.class, () -> sellerService.deleteSeller(2L));
        assertEquals("Seller not found with id - " + 2L, exception.getMessage());
    }

    @Test
    void verifyEmail() throws Exception{
        Mockito.when(sellerRepository.findByEmail("milan.juneja97@gmail.com")).thenReturn(seller);
        Mockito.when(sellerRepository.save(seller)).thenReturn(seller);
        Seller result = sellerService.verifyEmail("milan.juneja97@gmail.com", "123456");
        assertNotNull(result);
        assertTrue(result.isEmailVerified());
    }

    @Test
    void updateSellerAccountStatus() throws Exception{
        Mockito.when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        Mockito.when(sellerRepository.save(seller)).thenReturn(seller);

        Seller result = sellerService.updateSellerAccountStatus(1L, AccountStatus.ACTIVE);

        assertNotNull(result);
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());
        verify(sellerRepository).save(any(Seller.class));
    }

    @Test
    void saveAddress() throws Exception{
        Mockito.when(addressRepository.save(any(Address.class))).thenReturn(address);
        Address result = sellerService.saveAddress(address);
        assertNotNull(result);
        assertEquals("9999999999", result.getMobile());
    }
}