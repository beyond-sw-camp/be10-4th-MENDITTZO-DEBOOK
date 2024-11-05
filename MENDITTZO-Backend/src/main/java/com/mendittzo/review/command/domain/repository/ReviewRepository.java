package com.mendittzo.review.command.domain.repository;

import com.mendittzo.review.command.domain.aggregate.Review;

import java.util.Optional;

public interface ReviewRepository {
    Review save(Review newReview);

    Optional<Review> findById(Long reviewId);

    void deleteById(Long reviewId);
}
