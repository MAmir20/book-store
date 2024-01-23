package tn.enis.bookservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private int edition;
    private String isbn;
    private String language;
    private String image;
    private int rate;
    private double discount = 0;
    private Date date;
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    public Book(String name, double price, int quantity, Category category) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public double newPrice(){
        return getPrice() * (1 - getDiscount());
    }

    @Override
    public String toString() {
        return "Book{" + '\n' +
                "id= " + id + '\n' +
                ", name= '" + name + '\n' +
                ", price= " + price + '\n' +
                ", quantity= " + quantity + '\n' +
                '}';
    }

}
