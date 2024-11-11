package com.mendittzo.review.query.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewDTO {

    private final String title;
    private final String content;
    private final int rating;
    private final String nickname;
    private final LocalDateTime createDatetime;
    private final LocalDateTime updateDatetime;
    private final boolean isWriter;  // 작성자 여부
    private final Long userId;
    private final Long reviewId;
    private final Long bookId;
}
