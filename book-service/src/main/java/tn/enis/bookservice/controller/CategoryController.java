package tn.enis.bookservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.model.Category;
import tn.enis.bookservice.repository.CategoryRepository;
import tn.enis.bookservice.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookRepository bookRepository;
    @GetMapping({"/",""})
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> showCategory(@PathVariable Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryRepository.save(category);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category updatedCategory) {
        if (categoryRepository.existsById(categoryId)) {
            updatedCategory.setId(categoryId);
            Category updated=categoryRepository.save(updatedCategory);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/books/{categoryId}")
    public ResponseEntity<List<Book>> getAllBooksByCategory(@PathVariable Long categoryId){
        return categoryRepository.findById(categoryId)
                .map(category -> ResponseEntity.ok(bookRepository.findBooksByCategory(category)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Category> getCategoryFromBook(@PathVariable Long bookId){

            return bookRepository.findById(bookId)
                    .map(book -> ResponseEntity.ok(book.getCategory()))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}

