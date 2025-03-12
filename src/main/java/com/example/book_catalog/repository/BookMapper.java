package com.example.book_catalog.repository;

import com.example.book_catalog.model.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {

    @Select("SELECT * FROM books")
    List<Book> getAllBooks();

    @Select("SELECT * FROM books WHERE id = #{id}")
    Book getBookById(Long id);

    @Insert("INSERT INTO books (title, author, isbn, published_year) VALUES (#{title}, #{author}, #{isbn}, #{publishedYear})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBook(Book book);

    @Update("UPDATE books SET title = #{title}, author = #{author}, isbn = #{isbn}, published_year = #{publishedYear} WHERE id = #{id}")
    void updateBook(Book book);

    @Delete("DELETE FROM books WHERE id = #{id}")
    void deleteBook(Long id);

    @Select("SELECT * FROM books WHERE title LIKE #{keyword} OR author LIKE #{keyword}")
    List<Book> searchBooks(@Param("keyword") String keyword);

}

