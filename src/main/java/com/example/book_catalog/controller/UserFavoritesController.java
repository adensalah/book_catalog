package com.example.book_catalog.controller;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/favorites")
public class UserFavoritesController {

    private final BookService bookService;

    // Simulating a favorites list (ideally should be stored in a database)
    private final List<Book> favoriteBooks = new ArrayList<>();

    public UserFavoritesController(BookService bookService) {
        this.bookService = bookService;
    }

    // Show favorite books
    @GetMapping
    public String showFavorites(Model model) {
        model.addAttribute("favoriteBooks", favoriteBooks);
        return "user-favorites";
    }

    // Add a book to favorites
    @GetMapping("/add/{id}")
    public String addBookToFavorites(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null && !favoriteBooks.contains(book)) {
            favoriteBooks.add(book);
        }
        return "redirect:/favorites";
    }
}
