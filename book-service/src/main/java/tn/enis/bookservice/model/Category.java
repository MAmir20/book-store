package tn.enis.bookservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Override
    public String toString() {
        return "Category{" + '\n' +
                "id= " + id + '\n' +
                ", name= '" + name + '\n' +
                '}';
    }

}
