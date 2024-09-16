package com.gtasterix.E_Commerce.mapper;

import com.gtasterix.E_Commerce.dto.ReviewDTO;
import com.gtasterix.E_Commerce.model.Review;
import com.gtasterix.E_Commerce.model.Product;
import com.gtasterix.E_Commerce.model.User;

public class ReviewMapper {

    public static ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewID(review.getReviewID());
        dto.setProductID(review.getProduct().getProductID());
        dto.setUserID(review.getUser().getUserID());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewDate(review.getReviewDate());
        return dto;
    }

    public static Review toEntity(ReviewDTO dto, Product product, User user) {
        Review review = new Review();
        review.setReviewID(dto.getReviewID());
        review.setProduct(product);
        review.setUser(user);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setReviewDate(dto.getReviewDate());
        return review;
    }
}
