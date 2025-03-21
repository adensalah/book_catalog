package com.example.book_catalog.controller;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/book-details") // 🔹 Changed from "/books" to "/book-details"
public class BookViewController {

    private final BookService bookService;

    public BookViewController(BookService bookService) {
        this.bookService = bookService;
    }

    // 👁 View Book Details
    @GetMapping("/{id}") // Now accessible via "/book-details/{id}"
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/books?error=BookNotFound"; // Redirect if book not found
        }
        model.addAttribute("book", book);
        return "book-details"; // Ensure book-details.html exists
    }
}
