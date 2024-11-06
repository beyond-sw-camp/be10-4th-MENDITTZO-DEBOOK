package com.mendittzo.review.query.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class ReviewResponseDTO {

    private final String title;
    private final String content;
    private final int rating;
    private final String nickname;
    private final LocalDateTime createDatetime;

}
