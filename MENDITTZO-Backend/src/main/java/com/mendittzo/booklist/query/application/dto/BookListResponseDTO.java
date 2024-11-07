package com.mendittzo.booklist.query.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class BookListResponseDTO {

    private final String title;
    private final String author;
    private final String publisher;
    private final String img;
}
