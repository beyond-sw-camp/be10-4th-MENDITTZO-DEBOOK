package com.mendittzo.review.query.domain.repository;

import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.query.application.dto.ReviewResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ReviewQueryRepository extends JpaRepository<Review, Long> {

    List<ReviewResponseDTO> findReviewsByBookId(Long bookId);
}
