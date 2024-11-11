package com.mendittzo.booklist.query.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BookListListResponseDTO {

    private List<BookListResponseDTO> bookLists;
    private int totalPages;
    private int totalItems;
    private int currentPage;
    private int pageSize;
}
