package com.example.book_catalog.controller;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookViewController {

    private final BookService bookService;

    public BookViewController(BookService bookService) {
        this.bookService = bookService;
    }

    // Show all books and handle search
    @GetMapping
    public String listBooks(@RequestParam(name = "search", required = false) String search, Model model) {
        List<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search);
        } else {
            books = bookService.getAllBooks();
        }
        model.addAttribute("books", books);
        model.addAttribute("search", search);
        return "books"; // Return the Thymeleaf template (books.html)
    }

    // Handle adding a new book
    @PostMapping("/add")
    public String addBook(@RequestParam String title, @RequestParam String author,
                          @RequestParam String isbn, @RequestParam int publishedYear) {
        Book book = new Book(null, title, author, isbn, publishedYear);
        bookService.addBook(book);
        return "redirect:/books"; // Redirect to book list after adding
    }

    // Handle deleting a book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "edit-book"; // Thymeleaf edit form
    }

    // Handle updating a book
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam String author,
                             @RequestParam String isbn, @RequestParam int publishedYear) {
        Book book = new Book(id, title, author, isbn, publishedYear);
        bookService.updateBook(book);
        return "redirect:/books";
    }
}
