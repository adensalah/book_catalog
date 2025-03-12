package com.example.book_catalog.service;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.repository.BookMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookMapper bookMapper;

    // Constructor injection of BookMapper (MyBatis repository)
    public BookService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public List<Book> searchBooks(String keyword) {
        return bookMapper.searchBooks("%" + keyword + "%");
    }


    // Get all books
    public List<Book> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    // Get a book by ID
    public Book getBookById(Long id) {
        return bookMapper.getBookById(id);
    }

    // Add a new book
    public void addBook(Book book) {
        bookMapper.insertBook(book);
    }

    public void addListOfBooks(List<Book> books) {
        for (Book book: books) {
            bookMapper.insertBook(book);
        }
    }

    // Update an existing book
    public void updateBook(Book book) {
        bookMapper.updateBook(book);
    }

    // Delete a book
    public void deleteBook(Long id) {
        bookMapper.deleteBook(id);
    }
}

