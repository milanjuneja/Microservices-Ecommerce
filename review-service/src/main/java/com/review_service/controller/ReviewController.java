package com.review_service.controller;

import com.review_service.clients.ProductClient;
import com.review_service.clients.UserClient;
import com.review_service.dto.ProductDTO;
import com.review_service.dto.UserDTO;
import com.review_service.entity.Review;
import com.review_service.request.CreateReviewRequest;
import com.review_service.response.ApiResponse;
import com.review_service.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserClient userClient;

    private final ProductClient productClient;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(
            @PathVariable Long productId,
            @RequestHeader("X-User-Email") String email
    ) {
        return new ResponseEntity<>(reviewService.getReviewByProductId(productId), HttpStatus.OK);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Long productId,
            @RequestBody CreateReviewRequest req,
            @RequestHeader("X-User-Email") String email
            ) throws Exception {

        UserDTO user = userClient.findUserByEmail(email);
        ProductDTO product = productClient.getProductById(productId);

        return new ResponseEntity<>(reviewService.createReview(
                req, user, product
        ), HttpStatus.OK);

    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest req,
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        return new ResponseEntity<>(reviewService.updateReview(
                reviewId,
                req.getReviewText(),
                req.getReviewRating(),
                user.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("X-User-Email") String email
    ) throws Exception {
        UserDTO user = userClient.findUserByEmail(email);
        reviewService.deleteReview(reviewId, user.getId());

        ApiResponse res = new ApiResponse();
        res.setMessage("Review Deleted successfully");
        return ResponseEntity.ok(res);
    }


}