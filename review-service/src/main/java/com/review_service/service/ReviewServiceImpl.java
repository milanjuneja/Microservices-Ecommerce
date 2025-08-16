package com.review_service.service;

import com.review_service.dto.ProductDTO;
import com.review_service.dto.ReviewDTO;
import com.review_service.dto.UserDTO;
import com.review_service.entity.Review;
import com.review_service.repo.ReviewRepository;
import com.review_service.request.CreateReviewRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    @Override
    public Review createReview(CreateReviewRequest req, UserDTO user, ProductDTO product) {
        Review review = new Review();
        review.setUserId(user.getId());
        review.setProductId(product.getId());
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(ReviewDTO.toDTO(review));

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Review review = findReviewById(reviewId);
        if(review.getId().equals(userId)){
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);
        }
        throw new Exception("You can't update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = findReviewById(reviewId);
        if(!review.getId().equals(userId))
            throw new Exception("You can't delete this review");
        reviewRepository.delete(review);
    }

    @Override
    public Review findReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(() ->
                new Exception("Review not found"));
    }
}