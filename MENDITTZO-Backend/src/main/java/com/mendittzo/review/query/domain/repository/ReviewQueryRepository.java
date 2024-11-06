package com.mendittzo.review.query.domain.repository;

import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.query.application.dto.ReviewResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ReviewQueryRepository extends JpaRepository<Review, Long> {
    @Query("SELECT new com.mendittzo.review.query.application.dto.ReviewResponseDTO(r.title, r.content, r.rating, u.nickname, r.createDateTime) " +
            "FROM Review r JOIN User u ON r.userId = u.userId  WHERE r.bookId = :bookId")
    Page<ReviewResponseDTO> findReviewsByBookId(Long bookId, Pageable pageable);
}
