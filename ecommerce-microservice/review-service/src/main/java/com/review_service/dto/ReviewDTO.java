package com.review_service.dto;

import com.review_service.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private String reviewText;
    private double rating;
    private List<String> productImages;
    private Long productId;
    private Long userId;
    private LocalDateTime createdAt;
    public static ReviewDTO toDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getReviewText(),
                review.getRating(),
                review.getProductImages(),
                review.getProductId(),
                review.getUserId(),
                review.getCreatedAt()
        );
    }

    public static Review fromDTO(ReviewDTO dto) {
        Review review = new Review();
        review.setId(dto.getId());
        review.setReviewText(dto.getReviewText());
        review.setRating(dto.getRating());
        review.setProductImages(dto.getProductImages());
        review.setProductId(dto.getProductId());
        review.setUserId(dto.getUserId());
        review.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDateTime.now());
        return review;
    }
}