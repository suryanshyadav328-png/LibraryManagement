package com.college.library.controller;

import com.college.library.model.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    // Collection used as in-memory storage:
    // LinkedHashMap -> O(1) lookup by id, and keeps insertion order for "get all".
    private final Map<Integer, Book> books = new LinkedHashMap<>();

    public BookController() {
        // Sample data so GET works immediately
        books.put(1, new Book(1, "Effective Java", "Joshua Bloch", "Programming", 5));
        books.put(2, new Book(2, "Clean Code", "Robert C. Martin", "Programming", 3));
        books.put(3, new Book(3, "Wings of Fire", "A.P.J. Abdul Kalam", "Biography", 7));
    }

    // (b) POST - add a new book
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        if (book.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book id is required");
        }
        if (books.containsKey(book.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Book with id " + book.getId() + " already exists");
        }
        books.put(book.getId(), book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    // (c) GET - all books
    @GetMapping
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    // (c) GET - book by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Integer id) {
        Book book = books.get(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " not found");
        }
        return ResponseEntity.ok(book);
    }

    // (d) PUT - update title, author, category, availableCopies
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book updated) {
        Book existing = books.get(id);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " not found");
        }
        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setCategory(updated.getCategory());
        existing.setAvailableCopies(updated.getAvailableCopies());
        return ResponseEntity.ok(existing);
    }

    // (e) DELETE - delete by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        if (books.remove(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " not found");
        }
        return ResponseEntity.ok("Book with id " + id + " deleted successfully");
    }
}
