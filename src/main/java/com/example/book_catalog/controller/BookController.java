package com.example.book_catalog.controller;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String listBooks(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "book-list"; // Ensure book-list.html exists in src/main/resources/templates
    }
}
