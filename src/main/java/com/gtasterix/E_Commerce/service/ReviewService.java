package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.exception.ReviewNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.Review;
import com.gtasterix.E_Commerce.model.User;
import com.gtasterix.E_Commerce.repository.ProductRepository;
import com.gtasterix.E_Commerce.repository.ReviewRepository;
import com.gtasterix.E_Commerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Review createReview(Review review){
        try
        {
            validateReview(review);
            Product product = productRepository.findById(review.getProduct().getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + review.getProduct().getProductID() + " not found"));
            review.setProduct(product);
            User user = userRepository.findById(review.getUser().getUserID())
                    .orElseThrow(() -> new ValidationException("User with ID " + review.getUser().getUserID() + " not found"));
            review.setUser(user);
            return reviewRepository.save(review);
        }
        catch (ValidationException e) {
            throw new ValidationException("Failed to create review of user : " + e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while creating review of user: " + e.getMessage());
        }
    }


    public Review getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " not found"));
    }

    public Review updateReview(UUID id, Review review) {
        validateReview(review);
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review with ID " + id + " not found");
        }
        review.setReviewID(id);
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    private void validateReview(Review review) {
        if (review.getProduct() == null) {
            throw new ValidationException("Product cannot be null");
        }
        if (review.getUser() == null) {
            throw new ValidationException("User cannot be null");
        }
        if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
        if (review.getReviewDate() == null) {
            throw new ValidationException("Review date cannot be null");
        }
    }

    public Review patchReviewById(UUID id, Review review) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " not found"));

        if (review.getProduct() != null) existingReview.setProduct(review.getProduct());
        if (review.getUser() != null) existingReview.setUser(review.getUser());
        if (review.getRating() != null) {
            if (review.getRating() < 1 || review.getRating() > 5) {
                throw new ValidationException("Rating must be between 1 and 5");
            }
            existingReview.setRating(review.getRating());
        }
        if (review.getComment() != null) existingReview.setComment(review.getComment());
        if (review.getReviewDate() != null) existingReview.setReviewDate(review.getReviewDate());

        return reviewRepository.save(existingReview);
    }

    public void deleteReviewById(UUID id) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " not found"));

        reviewRepository.delete(existingReview);
    }
}
