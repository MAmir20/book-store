package tn.enis.bookservice.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.model.Author;
import tn.enis.bookservice.repository.AuthorRepository;
import tn.enis.bookservice.repository.BookRepository;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @PersistenceContext
    private EntityManager entityManager;

    // Get all authors
    @GetMapping({"", "/"})
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Get author by ID
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return authorRepository.findById(id)
                .map(author -> ResponseEntity.ok().body(author))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new author
    @PostMapping({"", "/"})
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    // Update an existing author
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(updatedAuthor.getName());
                    // Update other fields as needed
                    Author savedAuthor = authorRepository.save(author);
                    return ResponseEntity.ok().body(savedAuthor);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete an author
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    authorRepository.delete(author);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/books/{authorId}")
    public ResponseEntity<List<Book>> getAllBooksByAuthor(@PathVariable Long authorId) {
        return authorRepository.findById(authorId)
                .map(author -> ResponseEntity.ok().body(bookRepository.findBooksByAuthorsContaining(author)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/books/{authorId}/{bookId}")
    public ResponseEntity<Object> addBookToAuthor(@PathVariable Long authorId, @PathVariable Long bookId) {
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
    @DeleteMapping("/books/{authorId}/{bookId}")
    public ResponseEntity<Object> removeBookFromAuthor(@PathVariable Long authorId, @PathVariable Long bookId) {
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
}

