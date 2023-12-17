package tn.enis.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.enis.bookservice.model.Author;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.model.Category;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByNameContains(String c);
    List<Book> findByPriceGreaterThan(double price);
    List<Book> findBooksByCategory(Category c);
    List<Book> findBooksByAuthorsContaining(Author a);



}
