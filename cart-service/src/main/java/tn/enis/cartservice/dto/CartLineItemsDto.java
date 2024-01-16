package tn.enis.cartservice.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartLineItemsDto {
    private Long bookId;
    private Integer quantity;
    private BigDecimal price;
}
