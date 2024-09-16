package com.gtasterix.E_Commerce.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewDTO {
    private UUID reviewID;
    private UUID productID;
    private UUID userID;
    private Integer rating;
    private String comment;
    private LocalDateTime reviewDate;
}