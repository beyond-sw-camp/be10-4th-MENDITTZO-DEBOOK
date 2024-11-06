package com.mendittzo.booklist.query.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookDetailResponseDTO {

    private BookResponseDTO bookResponse;

    public BookDetailResponseDTO(BookResponseDTO bookResponse) {
        this.bookResponse = bookResponse;
    }
}

