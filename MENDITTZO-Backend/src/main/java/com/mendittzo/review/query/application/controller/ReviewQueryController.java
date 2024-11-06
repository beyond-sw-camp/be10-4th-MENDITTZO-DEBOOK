package com.mendittzo.review.query.application.controller;

import com.mendittzo.review.query.application.dto.ReviewListResponseDTO;
import com.mendittzo.review.query.application.service.ReviewQueryService;
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
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "createDateTime,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Order.desc("createDateTime")); // 기본값으로 최신순 설정

        if (sortParams.length == 2) {
            String field = sortParams[0];
            String direction = sortParams[1];

            // direction 값에 따라 정렬 방향 설정
            if ("createDateTime".equalsIgnoreCase(field) || "rating".equalsIgnoreCase(field)) {
                sortOrder = "desc".equalsIgnoreCase(direction)
                        ? Sort.by(Sort.Order.desc(field))
                        : Sort.by(Sort.Order.asc(field));
            }
        }

        Pageable pageable = PageRequest.of(page - 1, size, sortOrder);

        ReviewListResponseDTO response = reviewQueryService.getReviews(bookId, pageable);

        return ResponseEntity.ok(response);
    }
}
