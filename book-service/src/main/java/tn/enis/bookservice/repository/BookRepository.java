package tn.enis.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.model.Category;
import tn.enis.bookservice.model.Author;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByNameContains(String c);
    List<Book> findByPriceGreaterThan(double price);
    List<Book> findBooksByCategory(Category c);
    List<Book> findBooksByAuthorsContaining(Author a);



}
