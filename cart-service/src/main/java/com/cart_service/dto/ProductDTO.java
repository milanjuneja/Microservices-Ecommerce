package com.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private int discountPercentage;
    private int quantity;
    private String color;
    private List<String> images;
    private int numOfRatings;
    private String categoryId;  // Can be replaced with CategoryDTO if needed
    private Long sellerId;      // Can be replaced with SellerDTO if needed
    private LocalDate createdAt;
    private String sizes;

}
