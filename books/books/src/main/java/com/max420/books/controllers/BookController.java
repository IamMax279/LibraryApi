package com.max420.books.controllers;


import com.max420.books.dto.AddBookRequest;
import com.max420.books.services.BookService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/addbook")
    public ResponseEntity<String> addBook(@RequestBody AddBookRequest request) {
        try {
            bookService.addBook(request);
            return ResponseEntity.ok("added a new book successfully");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/getallbooks")
    public ResponseEntity<List<Map<String, String>>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/getbytitle")
    public ResponseEntity<Map<String, String>> getbytitle(@RequestParam String title) {
        try {
            return ResponseEntity.ok(bookService.findByTitle(title));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/rentabook")
    public ResponseEntity<String> rentABook(@RequestParam String title) {
        try {
            bookService.rentABook(title);
            return ResponseEntity.ok("successfully rented a book");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/bringbookback")
    public ResponseEntity<String> bringBookBack(@RequestParam String title) {
        try {
            bookService.bringBookBack(title);
            return ResponseEntity.ok("book has been brought back successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}