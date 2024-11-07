package com.mendittzo.review.command.application.controller;

import com.mendittzo.common.exception.SuccessCode;
import com.mendittzo.review.command.application.dto.ReviewCreateRequestDTO;
import com.mendittzo.review.command.application.dto.ReviewUpdateRequestDTO;
import com.mendittzo.review.command.application.service.ReviewCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewCommandController {

    private final ReviewCommandService reviewCommandService;

    @PostMapping("/reviews")
    public ResponseEntity<String> createReview(
            @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO
    ) {

        reviewCommandService.createReview(reviewCreateRequestDTO);

        return ResponseEntity.ok(SuccessCode.REVIEW_CREATE_SUCCESS.getMessage());
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO
    ) {

        reviewCommandService.updateReview(reviewId, reviewUpdateRequestDTO);

        return ResponseEntity.ok(SuccessCode.REVIEW_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {

        reviewCommandService.deleteReview(reviewId);

        return ResponseEntity.ok(SuccessCode.REVIEW_DELETE_SUCCESS.getMessage());
    }
}
