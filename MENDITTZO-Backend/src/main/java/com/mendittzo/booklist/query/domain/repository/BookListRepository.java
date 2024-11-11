package com.mendittzo.booklist.query.domain.repository;

import com.mendittzo.booklist.command.domain.aggregate.Book;
import com.mendittzo.booklist.query.application.dto.BookResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookListRepository extends JpaRepository<Book, Long> {

    @Query("SELECT new com.mendittzo.booklist.query.application.dto.BookResponseDTO(" +
            "b.bookId, b.title, b.img, b.author, b.publisher, b.pubdate, " +
            "CAST(ROUND(COALESCE(AVG(CASE WHEN r.status != 'DELETED' THEN r.rating ELSE NULL END), 0)) AS integer), " + // 삭제된 리뷰를 제외하고 정수형 평균 별점 계산
            "ROUND(COALESCE(AVG(CASE WHEN r.status != 'DELETED' THEN r.rating ELSE NULL END), 0), 2), " +  // 삭제된 리뷰를 제외한 평균 별점 (소수점 2자리)
            "SUM(CASE WHEN r.status != 'DELETED' THEN 1 ELSE 0 END), " + // 상태가 DELETED가 아닌 리뷰의 총 수
            "b.info) " +
            "FROM Book b JOIN Review r ON b.bookId = r.bookId " +
            "WHERE b.bookId = :bookId")
    Optional<BookResponseDTO> findBookByBookId(Long bookId);

}
