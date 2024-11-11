package com.mendittzo.review.query.application.service;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.review.query.application.dto.ReviewDTO;
import com.mendittzo.review.query.application.dto.ReviewDetailResponseDTO;
import com.mendittzo.review.query.application.dto.ReviewListResponseDTO;
import com.mendittzo.review.query.application.dto.ReviewResponseDTO;
import com.mendittzo.review.query.domain.repository.ReviewQueryRepository;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewQueryRepository reviewQueryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ReviewListResponseDTO getReviews(Long bookId, User currentUser, Pageable pageable) {

        // 페이지네이션 및 정렬 적용된 리뷰 조회
        Page<ReviewResponseDTO> page = reviewQueryRepository.findReviewsByBookId(bookId, pageable);

        // 각 리뷰에 대해 작성자인지 여부(isWriter) 설정
        List<ReviewDTO> reviews = page.getContent().stream()
                .map(review -> new ReviewDTO(
                        review.getTitle(),
                        review.getContent(),
                        review.getRating(),
                        review.getNickname(),
                        review.getCreateDatetime(),
                        currentUser != null && review.getUserId().equals(currentUser.getUserId()), // 작성자 여부 확인
                        review.getUserId()  // 사용자 ID도 추가
                ))
                .collect(Collectors.toList());

        return ReviewListResponseDTO.builder()
                .reviewList(reviews)  // 작성자 여부가 포함된 리뷰 목록
                .totalPages(page.getTotalPages())  // 전체 페이지 수
                .totalItems((int) page.getTotalElements())  // 전체 리뷰 수
                .currentPage(page.getNumber() + 1)  // 현재 페이지 (1-based)
                .pageSize(page.getSize())  // 한 페이지의 리뷰 수
                .build();
    }

    public ReviewListResponseDTO getMyReviews(Long loginId, Pageable pageable) {

        User loginUser = userRepository.findByLoginId(loginId);

        // 페이지네이션 및 정렬 적용된 리뷰 조회
        Page<ReviewResponseDTO> page = reviewQueryRepository.findReviewsByUserId(loginUser.getUserId(), pageable);

        // ReviewResponseDTO를 ReviewDTO로 변환
        List<ReviewDTO> reviews = page.getContent().stream()
                .map(review -> new ReviewDTO(
                        review.getTitle(),
                        review.getContent(),
                        review.getRating(),
                        review.getNickname(),
                        review.getCreateDatetime(),
                        review.getUserId().equals(loginUser.getUserId()), // 사용자 여부 확인
                        review.getUserId()  // 사용자 ID도 추가
                ))
                .collect(Collectors.toList());

        return ReviewListResponseDTO.builder()
                .reviewList(reviews)  // ReviewDTO 리스트를 반환
                .totalPages(page.getTotalPages())  // 전체 페이지 수
                .totalItems((int) page.getTotalElements())  // 전체 리뷰 수
                .currentPage(page.getNumber() + 1)  // 현재 페이지 (1-based)
                .pageSize(page.getSize())  // 한 페이지의 리뷰 수
                .build();
    }

    @Transactional(readOnly = true)
    public ReviewDetailResponseDTO getReview(Long reviewId) {

        ReviewResponseDTO review = reviewQueryRepository.findReviewByReviewId(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        return new ReviewDetailResponseDTO(review);
    }
}
