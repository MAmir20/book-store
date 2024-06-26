package tn.enis.bookservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.enis.bookservice.model.Book;
import tn.enis.bookservice.model.Category;
import tn.enis.bookservice.repository.CategoryRepository;
import tn.enis.bookservice.model.Author;
import tn.enis.bookservice.repository.AuthorRepository;
import tn.enis.bookservice.repository.BookRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookServiceApplication implements CommandLineRunner {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private AuthorRepository authorRepository;
	public static void main(String[] args) {

		SpringApplication.run(BookServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Category c1 = new Category(null, "Fantasy", "https://png.pngtree.com/png-vector/20230926/ourmid/pngtree-tree-fantasy-world-element-png-image_10147512.png");
		Category c2 = new Category(null, "Science Fiction","https://cdn.pixabay.com/photo/2016/11/16/14/24/science-fiction-1829010_1280.png");
		Category c3 = new Category(null, "Action & Adventure","https://www.traveliowa.com/userdocs/ogimages/outdoor_adventure_ugc_-brian_gibbs-_2.webp");
		Category c4 = new Category(null, "Detective & Mystery","r");
		categoryRepository.save(c1);
		categoryRepository.save(c2);
		categoryRepository.save(c3);
		Book b1 =new Book("The Magic Tree",4300,3,c1);
		Book b2 = new Book("Winter Fairy",1200,4,c1);
		Book b3 = new Book("Wizards of Ice",3200,32,c2);
		bookRepository.save(b1);
		bookRepository.save(b2);
		bookRepository.save(b3);
		Set<Book> books = new HashSet<>();
		books.add(b1);
		books.add(b2);
		Author a1 = new Author(null, "Hassen Akrout", "hassen.akrout@enis.tn",books);
		Author a2 = new Author( null,"Amir Mezghani", "amir.mezghani@enis.tn",books);
		Author a3 = new Author(null,"Mouna Ben Ayed", "benayed.mouna@enis.tn", books);
		authorRepository.save(a1);
		authorRepository.save(a2);
		authorRepository.save(a3);
		//List<Book> books = bookRepository.findAll();
		//books.forEach(o->{
		//	System.out.println(o.toString());});
	}
}
