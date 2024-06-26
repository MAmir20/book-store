package tn.enis.bookservice.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enis.bookservice.dto.BookResponse;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.repository.CategoryRepository;
import tn.enis.bookservice.model.Author;
import tn.enis.bookservice.repository.AuthorRepository;
import tn.enis.bookservice.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @GetMapping({"/",""})
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @GetMapping("/{bookId}")
    public ResponseEntity<Book> showBook(@PathVariable Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping({"","/"})
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book createdBook = bookRepository.save(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook) {
        if (bookRepository.existsById(bookId)) {
            updatedBook.setId(bookId);
            Book updated=bookRepository.save(updatedBook);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookRepository.deleteById(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/category/{categoryId}")
    public List<Book> getAllBooksByCategory(@PathVariable Long categoryId){
        return bookRepository.findBooksByCategory(categoryRepository.findById(categoryId).get());
    }
    @Transactional
    @GetMapping("/authors/{bookId}")
    public ResponseEntity<List<Author>> getAuthorsByBook(@PathVariable Long bookId) {
        return bookRepository.findById(bookId)
                .map(book -> ResponseEntity.ok().body(authorRepository.findAuthorsByBooksContains(book)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/authors/{bookId}/{authorId}")
    public ResponseEntity<Object> addAuthorToBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        return bookRepository.findById(bookId)
                .flatMap(book -> authorRepository.findById(authorId)
                        .map(author -> {
                            book.getAuthors().add(author);
                            bookRepository.save(book);
                            return ResponseEntity.noContent().build();
                        }))
                .orElse(ResponseEntity.notFound().build());
    }
    @Transactional
    @DeleteMapping("/authors/{bookId}/{authorId}")
    public ResponseEntity<Object> removeAuthorFromBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        bookRepository.findById(bookId)
                .flatMap(book -> authorRepository.findById(authorId)
                        .map(author -> {
                            book.getAuthors().remove(author);
                            bookRepository.save(book);
                            return ResponseEntity.noContent().build();
                        }))
                .orElse(ResponseEntity.notFound().build());
        int deletedRows = entityManager.createNativeQuery(
                        "DELETE FROM book_author WHERE book_id = :bookId AND author_id = :authorId")
                .setParameter("bookId", bookId)
                .setParameter("authorId", authorId)
                .executeUpdate();
        if (deletedRows > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/exists")
    @ResponseStatus(HttpStatus.OK)
    public List<BookResponse> exists(@RequestBody Map<Long, Integer> books) {
        List<Long> ids = new ArrayList<>(books.keySet());
        return bookRepository.findByIdIn(ids)
                .stream()
                .map(book ->
                        BookResponse.builder()
                                .id(book.getId())
                                .quantity_in_stock(book.getQuantity())
                                .inStock(book.getQuantity() >= books.get(book.getId()))
                                .build()
                ).toList();
    }

    @PutMapping("/reduceQty")
    @ResponseStatus(HttpStatus.OK)
    public boolean reduceQty(@RequestBody Map<Long, Integer> books) {
        List<Long> ids = new ArrayList<>(books.keySet());
        ids.forEach(id -> {
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent()) {
                Book b = book.get();
                b.setQuantity(b.getQuantity() - books.get(id));
                bookRepository.save(b);
            }
        });
        return true;
    }
}

