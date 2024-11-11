package com.mendittzo.booklist.query.application.service;

import com.mendittzo.booklist.query.application.dto.BookDetailResponseDTO;
import com.mendittzo.booklist.query.application.dto.BookResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.stream.Stream;

@SpringBootTest
class BookListQueryServiceTest {

    @Autowired
    private BookListQueryService bookListQueryService;

    private static Stream<Arguments> getBookList() {

        return Stream.of(
                Arguments.of(
                        PageRequest.of(0, 5)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getBookList")
    void testGetBookList(Pageable pageable) {

        Assertions.assertDoesNotThrow(
                () -> bookListQueryService.getBookList(pageable)
        );
    }

    @Test
    void testGetBook() {
        Assertions.assertDoesNotThrow(
                () -> {
                    BookDetailResponseDTO response = bookListQueryService.getBook(6352228L);
                    System.out.println(response.getBookResponse());
                }
        );
    }
  
}