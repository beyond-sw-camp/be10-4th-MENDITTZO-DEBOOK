package com.mendittzo.booklist.query.domain.repository;

import com.mendittzo.booklist.command.domain.aggregate.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookListRepository extends JpaRepository<Book, Long> {

}
