package com.example.book_catalog.repository;

import com.example.book_catalog.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // ✅ Search books by title or author
    List<Book> findByTitleContainingOrAuthorContaining(String title, String author);
}
