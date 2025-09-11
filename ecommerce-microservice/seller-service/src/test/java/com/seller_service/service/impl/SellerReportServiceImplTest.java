package com.seller_service.service.impl;

import com.seller_service.entity.Seller;
import com.seller_service.entity.SellerReport;
import com.seller_service.model.USER_ROLE;
import com.seller_service.repo.SellerReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class SellerReportServiceImplTest {

    @Mock private SellerReportRepository sellerReportRepository;

    @InjectMocks
    private SellerReportServiceImpl sellerReportService;
    private Seller seller;
    private SellerReport sellerReport;
    @BeforeEach
    void setup(){

        seller = new Seller();
        seller.setId(1L);
        seller.setEmail("seller@example.com");
        seller.setRole(USER_ROLE.ROLE_SELLER);

        sellerReport = new SellerReport();
        sellerReport.setId(1L);
        sellerReport.setSeller(seller);

        MockitoAnnotations.openMocks(this);


    }

    @Test
    void getSellerReport_success() {

        Mockito.when(sellerReportRepository.findBySellerId(1L)).thenReturn(sellerReport);

        SellerReport result = sellerReportService.getSellerReport(seller);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(sellerReportRepository, never()).save(any(SellerReport.class));

    }

    @Test
    void getSellerReport_failure() {

        Mockito.when(sellerReportRepository.findBySellerId(1L)).thenReturn(null);
        Mockito.when(sellerReportRepository.save(any(SellerReport.class))).thenReturn(sellerReport);

        SellerReport result = sellerReportService.getSellerReport(seller);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(seller, result.getSeller());
        verify(sellerReportRepository).save(any(SellerReport.class));

    }

    @Test
    void updateSellerReport() {
        Mockito.when(sellerReportRepository.save(sellerReport)).thenReturn(sellerReport);
        SellerReport result = sellerReportService.updateSellerReport(sellerReport);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(sellerReportRepository).save(any(SellerReport.class));

    }
}