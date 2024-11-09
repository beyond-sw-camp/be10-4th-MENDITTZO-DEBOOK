package com.mendittzo.review.query.application.controller;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.review.query.application.dto.ReviewListResponseDTO;
import com.mendittzo.review.query.application.service.ReviewQueryService;
import com.mendittzo.security.util.UserUtil;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.query.domain.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewQueryController {

    private final ReviewQueryService reviewQueryService;
    private final UserQueryRepository userQueryRepository;

    @GetMapping("/{bookId}")
    public ResponseEntity<ReviewListResponseDTO> getReviews(
            @PathVariable(name = "bookId") Long bookId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        Long loginId = UserUtil.getCurrentUserLoginId();

        User currentUser = null;
        if (loginId != null) {
            currentUser = userQueryRepository.findUserInfoByLoginId(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        }

        ReviewListResponseDTO response = reviewQueryService.getReviews(bookId, currentUser, pageable);

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
