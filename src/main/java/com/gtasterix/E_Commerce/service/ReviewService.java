package com.gtasterix.E_Commerce.service;

import com.gtasterix.E_Commerce.dto.ReviewDTO;
import com.gtasterix.E_Commerce.exception.ReviewNotFoundException;
import com.gtasterix.E_Commerce.exception.ValidationException;
import com.gtasterix.E_Commerce.mapper.ReviewMapper;
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

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        try {
            validateReview(reviewDTO);
            Product product = productRepository.findById(reviewDTO.getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + reviewDTO.getProductID() + " not found"));
            User user = userRepository.findById(reviewDTO.getUserID())
                    .orElseThrow(() -> new ValidationException("User with ID " + reviewDTO.getUserID() + " not found"));
            Review review = ReviewMapper.toEntity(reviewDTO, product, user);
            Review savedReview = reviewRepository.save(review);
            return ReviewMapper.toDTO(savedReview);
        } catch (ValidationException e) {
            throw new ValidationException("Failed to create review: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while creating review: " + e.getMessage());
        }
    }

    public ReviewDTO getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " not found"));
        return ReviewMapper.toDTO(review);
    }

    public ReviewDTO updateReview(UUID id, ReviewDTO reviewDTO) {
        validateReview(reviewDTO);
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException("Review with ID " + id + " not found");
        }
        Product product = productRepository.findById(reviewDTO.getProductID())
                .orElseThrow(() -> new ValidationException("Product with ID " + reviewDTO.getProductID() + " not found"));
        User user = userRepository.findById(reviewDTO.getUserID())
                .orElseThrow(() -> new ValidationException("User with ID " + reviewDTO.getUserID() + " not found"));
        Review review = ReviewMapper.toEntity(reviewDTO, product, user);
        review.setReviewID(id);
        Review updatedReview = reviewRepository.save(review);
        return ReviewMapper.toDTO(updatedReview);
    }

    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream().map(ReviewMapper::toDTO).toList();
    }

    private void validateReview(ReviewDTO reviewDTO) {
        if (reviewDTO.getProductID() == null) {
            throw new ValidationException("Product ID cannot be null");
        }
        if (reviewDTO.getUserID() == null) {
            throw new ValidationException("User ID cannot be null");
        }
        if (reviewDTO.getRating() == null || reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
        if (reviewDTO.getReviewDate() == null) {
            throw new ValidationException("Review date cannot be null");
        }
    }

    public ReviewDTO patchReviewById(UUID id, ReviewDTO reviewDTO) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " not found"));

        if (reviewDTO.getProductID() != null) {
            Product product = productRepository.findById(reviewDTO.getProductID())
                    .orElseThrow(() -> new ValidationException("Product with ID " + reviewDTO.getProductID() + " not found"));
            existingReview.setProduct(product);
        }
        if (reviewDTO.getUserID() != null) {
            User user = userRepository.findById(reviewDTO.getUserID())
                    .orElseThrow(() -> new ValidationException("User with ID " + reviewDTO.getUserID() + " not found"));
            existingReview.setUser(user);
        }
        if (reviewDTO.getRating() != null) {
            if (reviewDTO.getRating() < 1 || reviewDTO.getRating() > 5) {
                throw new ValidationException("Rating must be between 1 and 5");
            }
            existingReview.setRating(reviewDTO.getRating());
        }
        if (reviewDTO.getComment() != null) existingReview.setComment(reviewDTO.getComment());
        if (reviewDTO.getReviewDate() != null) existingReview.setReviewDate(reviewDTO.getReviewDate());

        Review updatedReview = reviewRepository.save(existingReview);
        return ReviewMapper.toDTO(updatedReview);
    }

    public void deleteReviewById(UUID id) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " not found"));

        reviewRepository.delete(existingReview);
    }
}
