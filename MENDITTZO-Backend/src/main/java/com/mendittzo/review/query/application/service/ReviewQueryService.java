package com.mendittzo.review.query.application.service;

import com.mendittzo.review.query.application.dto.ReviewListResponseDTO;
import com.mendittzo.review.query.application.dto.ReviewResponseDTO;
import com.mendittzo.review.query.domain.repository.ReviewQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewQueryRepository reviewQueryRepository;

    public ReviewListResponseDTO getReviews(Long bookId) {

        List<ReviewResponseDTO> reviews = reviewQueryRepository.findReviewsByBookId(bookId);

        return ReviewListResponseDTO.builder()
                .reviewList(reviews)
                .build();
    }
}
