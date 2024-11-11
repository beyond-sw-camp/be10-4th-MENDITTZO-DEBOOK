package com.mendittzo.review.command.mapper;

import com.mendittzo.review.command.application.dto.ReviewCreateRequestDTO;
import com.mendittzo.review.command.domain.aggregate.Review;

public class ReviewMapper {
    public static Review toEntity(ReviewCreateRequestDTO reviewCreateRequestDTO, Long userId, Long bookId) {
        return Review.create(
                bookId,
                userId,
                reviewCreateRequestDTO.getTitle(),
                reviewCreateRequestDTO.getContent(),
                reviewCreateRequestDTO.getRating()
        );
    }
}
