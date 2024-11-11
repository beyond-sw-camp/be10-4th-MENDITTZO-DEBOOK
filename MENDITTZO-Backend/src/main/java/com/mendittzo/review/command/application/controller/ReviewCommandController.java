package com.mendittzo.review.command.application.controller;

import com.mendittzo.booklist.query.application.dto.BookResponseDTO;
import com.mendittzo.booklist.query.domain.repository.BookListRepository;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.common.exception.SuccessCode;
import com.mendittzo.review.command.application.dto.ReviewCreateRequestDTO;
import com.mendittzo.review.command.application.dto.ReviewUpdateRequestDTO;
import com.mendittzo.review.command.application.service.ReviewCommandService;
import com.mendittzo.security.util.UserUtil;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.query.domain.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReviewCommandController {

    private final ReviewCommandService reviewCommandService;
    private final UserQueryRepository userQueryRepository;
    private final BookListRepository bookListRepository;

    @PostMapping("/{bookId}/reviews")
    public ResponseEntity<String> createReview(
            @RequestBody ReviewCreateRequestDTO reviewCreateRequestDTO,
            @PathVariable Long bookId
    ) {

        Long loginId = UserUtil.getCurrentUserLoginId();

        User currentUser = userQueryRepository.findUserInfoByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        BookResponseDTO book = bookListRepository.findBookByBookId(bookId)
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        reviewCommandService.createReview(reviewCreateRequestDTO, currentUser.getUserId(), book.getBookId());

        return ResponseEntity.ok(SuccessCode.REVIEW_CREATE_SUCCESS.getMessage());
    }

    @PutMapping("/{bookId}/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable Long reviewId,
            @PathVariable Long bookId,
            @RequestBody ReviewUpdateRequestDTO reviewUpdateRequestDTO
    ) {

        reviewCommandService.updateReview(reviewId, reviewUpdateRequestDTO);

        return ResponseEntity.ok(SuccessCode.REVIEW_UPDATE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{bookId}/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long reviewId,
            @PathVariable Long bookId
    ) {

        reviewCommandService.deleteReview(reviewId);

        return ResponseEntity.ok(SuccessCode.REVIEW_DELETE_SUCCESS.getMessage());
    }
}
