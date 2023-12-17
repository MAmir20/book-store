package tn.enis.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContains(String c);

}
