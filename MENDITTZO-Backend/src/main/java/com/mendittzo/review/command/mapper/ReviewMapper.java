package com.mendittzo.review.command.mapper;

import com.mendittzo.review.command.application.dto.ReviewCreateRequestDTO;
import com.mendittzo.review.command.domain.aggregate.Review;

public class ReviewMapper {
    public static Review toEntity(ReviewCreateRequestDTO reviewCreateRequestDTO, Long loginId) {
        return Review.create(
                reviewCreateRequestDTO.getBookId(),
                loginId,
                reviewCreateRequestDTO.getTitle(),
                reviewCreateRequestDTO.getContent(),
                reviewCreateRequestDTO.getRating()
        );
    }
}
