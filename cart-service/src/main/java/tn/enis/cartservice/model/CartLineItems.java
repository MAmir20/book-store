package tn.enis.cartservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long BookId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
