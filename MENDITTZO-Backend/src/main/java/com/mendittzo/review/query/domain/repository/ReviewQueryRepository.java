package com.mendittzo.review.query.domain.repository;

import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.query.application.dto.ReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReviewQueryRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new com.mendittzo.review.query.application.dto.ReviewResponseDTO(r.reviewId, r.title, r.content, r.rating, u.nickname, r.createDatetime, r.userId) " +
            "FROM Review r JOIN User u ON r.userId = u.userId  WHERE r.bookId = :bookId AND NOT r.status = 'DELETED'")
    Page<ReviewResponseDTO> findReviewsByBookId(Long bookId, Pageable pageable);

    @Query("SELECT new com.mendittzo.review.query.application.dto.ReviewResponseDTO(r.reviewId, r.title, r.content, r.rating, u.nickname, r.createDatetime, r.userId) " +
            "FROM Review r JOIN User u ON r.userId = u.userId  WHERE r.userId = :userId AND NOT r.status = 'DELETED'")
    Page<ReviewResponseDTO> findReviewsByUserId(Long userId, Pageable pageable);

    @Query("SELECT new com.mendittzo.review.query.application.dto.ReviewResponseDTO(r.reviewId, r.title, r.content, r.rating, u.nickname, r.createDatetime, r.userId) " +
            "FROM Review r JOIN User u ON r.userId = u.userId WHERE r.reviewId = :reviewId AND NOT r.status = 'DELETED'")
    Optional<ReviewResponseDTO> findReviewByReviewId(Long reviewId);
}
