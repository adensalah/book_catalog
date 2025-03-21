package com.example.book_catalog.service;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ✅ Get all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // ✅ Get book by ID
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    // ✅ Add book
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    // ✅ Update book
    public void updateBook(Book book) {
        bookRepository.save(book);
    }

    // ✅ Delete book
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    // ✅ **New: Search books by title or author**
    public List<Book> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingOrAuthorContaining(keyword, keyword);
    }
}
