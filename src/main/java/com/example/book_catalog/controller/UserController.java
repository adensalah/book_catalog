package com.example.book_catalog.controller;

import com.example.book_catalog.model.Book;
import com.example.book_catalog.model.FavoriteBook;
import com.example.book_catalog.model.User;
import com.example.book_catalog.repository.FavoriteBookRepository;
import com.example.book_catalog.repository.UserRepository;
import com.example.book_catalog.service.BookService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("user/books")
public class UserController {

    private final BookService bookService;
    private final FavoriteBookRepository favoriteBookRepository;
    private final UserRepository userRepository;

    public UserController(BookService bookService, FavoriteBookRepository favoriteBookRepository, UserRepository userRepository) {
        this.bookService = bookService;
        this.favoriteBookRepository = favoriteBookRepository;
        this.userRepository = userRepository;
    }

    // Show all books for the user
    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "user-books"; // User-only book view
    }

    // View book details
    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return "redirect:/user/books"; // Redirect if book not found
        }
        model.addAttribute("book", book);
        return "book-details";
    }
    //Add book to favorite
    @GetMapping("/favorites/add/{id}")
    public String addBookToFavorites(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        Book book = bookService.getBookById(id);

        if (user.isPresent() && book != null) {
            // Check if the book is already in the user's favorites
            boolean alreadyFavorited = favoriteBookRepository.findByUser(user.get())
                    .stream()
                    .anyMatch(fav -> fav.getBook().getId().equals(id));

            if (!alreadyFavorited) {
                FavoriteBook favorite = new FavoriteBook();
                favorite.setUser(user.get());
                favorite.setBook(book); // ✅ Ensure the book is set before saving
                favoriteBookRepository.save(favorite);
            }
        }

        return "redirect:/user/books/favorites";
    }


    // Show user's favorite books
    @GetMapping("/favorites")
    public String showFavoriteBooks(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isPresent()) {
            List<FavoriteBook> favoriteBooks = favoriteBookRepository.findByUser(user.get());
            model.addAttribute("favoriteBooks", favoriteBooks);
        }

        return "user-favorites";
    }
}
