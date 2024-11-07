package com.mendittzo.booklist.query.application.service;

import com.mendittzo.booklist.command.domain.aggregate.Book;
import com.mendittzo.booklist.query.application.dto.BookDetailResponseDTO;
import com.mendittzo.booklist.query.application.dto.BookListListResponseDTO;
import com.mendittzo.booklist.query.application.dto.BookListResponseDTO;
import com.mendittzo.booklist.query.application.dto.BookResponseDTO;
import com.mendittzo.booklist.query.domain.repository.BookListRepository;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookListQueryService {

    private final BookListRepository bookListRepository;

    @Transactional(readOnly = true)
    public BookListListResponseDTO getBookList(Pageable pageable) {
        Page<Book> page = bookListRepository.findAll(pageable);

        List<BookListResponseDTO> bookLists = page.getContent().stream()
                .map(book -> BookListResponseDTO.builder()
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .img(book.getImg()) // 이미지 필드 이름 확인
                        .build())
                .collect(Collectors.toList());

        return BookListListResponseDTO.builder()
                .bookLists(bookLists)
                .totalPages(page.getTotalPages())
                .totalItems((int) page.getTotalElements()) // 수정된 totalItems 필드 사용
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .build();
    }

    @Transactional(readOnly = true)
    public BookDetailResponseDTO getBook(Long bookId) {

        BookResponseDTO book = bookListRepository.findBookByBookId(bookId);

        if(book == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
        }

        return new BookDetailResponseDTO(book);
    }
}
