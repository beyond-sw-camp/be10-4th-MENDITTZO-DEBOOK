package com.mendittzo.booklist.query.application.controller;

import com.mendittzo.booklist.query.application.dto.BookDetailResponseDTO;
import com.mendittzo.booklist.query.application.dto.BookListListResponseDTO;
import com.mendittzo.booklist.query.application.dto.BookListResponseDTO;
import com.mendittzo.booklist.query.application.service.BookListQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BookListQueryController {

    private final BookListQueryService bookListQueryService;

    @GetMapping("/booklists")
    public ResponseEntity<BookListListResponseDTO> getBookList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        BookListListResponseDTO response = bookListQueryService.getBookList(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/booklists/{bookId}")
    public ResponseEntity<BookDetailResponseDTO> getBook(@PathVariable Long bookId) {

        BookDetailResponseDTO response = bookListQueryService.getBook(bookId);

        return ResponseEntity.ok(response);
    }
}
