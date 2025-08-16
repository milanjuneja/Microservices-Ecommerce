package com.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int mrpPrice;
    private int sellingPrice;
    private int discountPercentage;
    private int quantity;
    private String color;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    private int numOfRatings;

    // Replacing direct Category relation with just ID
    private String categoryId;

    // Replace Seller object with just sellerId
    private Long sellerId;

    private LocalDate createdAt;
    private String sizes;

    // Remove Review dependency, handle via review-service
}
