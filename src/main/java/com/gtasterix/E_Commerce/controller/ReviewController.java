package com.gtasterix.E_Commerce.controller;

import com.gtasterix.E_Commerce.Util.Response;
import com.gtasterix.E_Commerce.dto.ReviewDTO;
import com.gtasterix.E_Commerce.exception.ReviewNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Response> createReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO createdReview = reviewService.createReview(reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Response("Review created successfully", createdReview, false));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getReviewById(@PathVariable UUID id) {
        try {
            ReviewDTO reviewDTO = reviewService.getReviewById(id);
            return ResponseEntity.ok(new Response("Review retrieved successfully", reviewDTO, false));
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateReview(@PathVariable UUID id, @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
            return ResponseEntity.ok(new Response("Review updated successfully", updatedReview, false));
        } catch (ReviewNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteReviewById(@PathVariable UUID id) {
        try {
            reviewService.deleteReviewById(id);
            return ResponseEntity.ok(new Response("Review deleted successfully", "Review erased", false));
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @GetMapping
    public ResponseEntity<Response> getAllReviews() {
        try {
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            return ResponseEntity.ok(new Response("Reviews retrieved successfully", reviews, false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> patchReviewById(@PathVariable UUID id, @RequestBody ReviewDTO reviewDTO) {
        try {
            ReviewDTO updatedReview = reviewService.patchReviewById(id, reviewDTO);
            return ResponseEntity.ok(new Response("Review updated successfully", updatedReview, false));
        } catch (ReviewNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(e.getMessage(), "An error occurred", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response("An error occurred", e.getMessage(), true));
        }
    }
}
