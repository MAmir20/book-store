package tn.enis.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.bookservice.model.Author;
import tn.enis.bookservice.model.Book;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameContains(String c);
    List<Author> findAuthorsByBooksContains(Book b);
}
