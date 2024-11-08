package com.mendittzo.review.query.application.controller;

import com.mendittzo.review.query.application.dto.ReviewListResponseDTO;
import com.mendittzo.review.query.application.service.ReviewQueryService;
import com.mendittzo.security.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/{bookId}")
    public ResponseEntity<ReviewListResponseDTO> getReviews(
            @PathVariable(name = "bookId") Long bookId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        ReviewListResponseDTO response = reviewQueryService.getReviews(bookId, pageable);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/user")
    public ResponseEntity<ReviewListResponseDTO> getMyReviews(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Long loginId = UserUtil.getCurrentUserLoginId();

        Pageable pageable = PageRequest.of(page - 1, size);

        ReviewListResponseDTO response = reviewQueryService.getMyReviews(loginId, pageable);

        return ResponseEntity.ok(response);
    }
}
