package tn.enis.cartservice.dto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.enis.cartservice.model.CartLineItems;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private Long userId;
    private Date date;
    private List<CartLineItemsDto> cartLineItemsDtoList;
    private Long status;
}
