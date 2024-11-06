package com.mendittzo.review.query.application.controller;

import com.mendittzo.review.query.application.dto.ReviewListResponseDTO;
import com.mendittzo.review.query.application.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/{bookId}")
    public ResponseEntity<ReviewListResponseDTO> getReviews(@PathVariable(name = "bookId") Long bookId) {

        ReviewListResponseDTO response = reviewQueryService.getReviews(bookId);

        return ResponseEntity.ok(response);
    }
}
