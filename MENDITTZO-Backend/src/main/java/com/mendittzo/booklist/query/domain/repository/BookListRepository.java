package com.mendittzo.booklist.query.domain.repository;

import com.mendittzo.booklist.command.domain.aggregate.Book;
import com.mendittzo.booklist.query.application.dto.BookResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookListRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new com.mendittzo.booklist.query.application.dto.BookResponseDTO(" +
            "b.title, b.author, b.publisher, b.pubdate, " +
            "CAST(ROUND(COALESCE(AVG(r.rating), 0)) AS integer), " + // 정수형 별점 (int)
            "ROUND(COALESCE(AVG(r.rating), 0), 1), " +  // 평균 별점 (double)
            "COUNT(r), " +
            "b.info)" + // 리뷰 총 수
            "FROM Book b JOIN Review r ON b.bookId = r.bookId " +  // JOIN 사용
            "WHERE b.bookId = :bookId")
    BookResponseDTO findBookByBookId(Long bookId);
}
