package com.mendittzo.review.query.application.service;

import com.mendittzo.review.query.application.dto.ReviewDetailResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewQueryServiceTest {

    @Autowired
    private ReviewQueryService reviewQueryService;

    @Test
    void testGetReview() {
        Assertions.assertDoesNotThrow(
                () -> {
                    ReviewDetailResponseDTO response = reviewQueryService.getReview(1L);
                    System.out.println(response.getClass());
                }
        );
    }



}