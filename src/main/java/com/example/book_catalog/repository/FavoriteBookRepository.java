package com.example.book_catalog.repository;

import com.example.book_catalog.model.FavoriteBook;
import com.example.book_catalog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteBookRepository extends JpaRepository<FavoriteBook, Long> {
    List<FavoriteBook> findByUser(User user);
}
