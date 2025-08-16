package com.seller_service.service;

import com.seller_service.entity.Seller;
import com.seller_service.entity.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);

}