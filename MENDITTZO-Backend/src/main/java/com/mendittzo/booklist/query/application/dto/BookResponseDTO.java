package com.mendittzo.booklist.query.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BookResponseDTO {

    private final String title;
    private final String author;
    private final String publisher;
    private final String pubdate;
    private final int avgRating;
    private final double avgRatingNumber;
    private final Long reviewCount;
    private final String info;
}
