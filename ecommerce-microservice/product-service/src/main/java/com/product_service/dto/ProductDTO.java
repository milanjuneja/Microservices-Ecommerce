package com.product_service.dto;

import com.product_service.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public static ProductDTO from(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getMrpPrice(),
            product.getSellingPrice(),
            product.getDiscountPercentage(),
            product.getQuantity(),
            product.getColor(),
            product.getImages(),
            product.getNumOfRatings(),
            product.getCategoryId(),
            product.getSellerId(),
            product.getCreatedAt(),
            product.getSizes()
        );
    }
    public Product to() {
        Product product = new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setDescription(this.description);
        product.setMrpPrice(this.mrpPrice);
        product.setSellingPrice(this.sellingPrice);
        product.setDiscountPercentage(this.discountPercentage);
        product.setQuantity(this.quantity);
        product.setColor(this.color);
        product.setImages(this.images);
        product.setNumOfRatings(this.numOfRatings);
        product.setCategoryId(this.categoryId);
        product.setSellerId(this.sellerId);
        product.setCreatedAt(this.createdAt);
        product.setSizes(this.sizes);
        return product;
    }
}