package com.review_service.service;

import com.review_service.dto.ProductDTO;
import com.review_service.dto.UserDTO;
import com.review_service.entity.Review;
import com.review_service.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req, UserDTO user, ProductDTO product);

    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;

    void deleteReview(Long reviewId, Long userId) throws Exception;

    Review findReviewById(Long reviewId) throws Exception;


}