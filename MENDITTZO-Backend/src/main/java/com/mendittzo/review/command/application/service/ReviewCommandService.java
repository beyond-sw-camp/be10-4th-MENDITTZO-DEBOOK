package com.mendittzo.review.command.application.service;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.review.command.application.dto.ReviewCreateRequestDTO;
import com.mendittzo.review.command.application.dto.ReviewUpdateRequestDTO;
import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.command.domain.repository.ReviewRepository;
import com.mendittzo.review.command.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public void createReview(ReviewCreateRequestDTO reviewCreateRequestDTO, Long userId, Long bookId) {

        Review newReview = ReviewMapper.toEntity(reviewCreateRequestDTO, userId, bookId);

        reviewRepository.save(newReview);
    }

    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequestDTO reviewUpdateRequestDTO) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.updateReviewDetails(
                reviewUpdateRequestDTO.getTitle(),
                reviewUpdateRequestDTO.getContent(),
                reviewUpdateRequestDTO.getRating()
        );
    }

    @Transactional
    public void deleteReview(Long reviewId) {

        reviewRepository.deleteById(reviewId);
    }
}
