package com.mendittzo.review.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewCreateRequestDTO {

    private final String title;
    private final String content;
    private final int rating;
}
