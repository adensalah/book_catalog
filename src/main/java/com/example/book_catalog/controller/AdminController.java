package com.example.book_catalog.controller;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    // Show admin book list
    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin-dashboard"; // Load admin page
    }

    // Show add book form
    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book()); // Empty book object
        return "add-book"; // Load add-book.html
    }

    // Save new book
    @PostMapping("/add")
    public String saveBook(@ModelAttribute Book book) {
        bookService.addBook(book);
        return "redirect:/admin?success=BookAdded";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/admin?error=BookNotFound"; // Redirect if book not found
        }
        model.addAttribute("book", book);
        return "edit-book"; // Load edit-book.html
    }

    // Update book
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String author,
                             @RequestParam String isbn,
                             @RequestParam int publishedYear) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/admin?error=BookNotFound";
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublishedYear(publishedYear);

        bookService.updateBook(book);
        return "redirect:/admin?success=BookUpdated";
    }

    // Delete book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/admin?error=BookNotFound";
        }

        bookService.deleteBook(id);
        return "redirect:/admin?success=BookDeleted";
    }
}

