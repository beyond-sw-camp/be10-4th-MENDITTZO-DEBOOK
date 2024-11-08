package com.mendittzo.review.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ReviewCreateRequestDTO {

    private final Long bookId;
    private final String title;
    private final String content;
    private final int rating;
}
