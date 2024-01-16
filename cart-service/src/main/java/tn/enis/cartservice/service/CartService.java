package tn.enis.cartservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enis.cartservice.dto.CartLineItemsDto;
import tn.enis.cartservice.dto.CartRequest;
import tn.enis.cartservice.dto.CartResponse;
import tn.enis.cartservice.model.Cart;
import tn.enis.cartservice.model.CartLineItems;
import tn.enis.cartservice.repository.CartRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private  final CartRepository cartRepository;
    public void newCart(CartRequest cartRequest) {
        Cart cart = new Cart();
        cart.setUserId(cartRequest.getUserId());
        cart.setDate(cartRequest.getDate());
        cart.setStatus(0L);

        List<CartLineItems> cartLineItems = cartRequest.getCartLineItemsDtoList().stream().map(cartLineItemsDto -> mapToDto(cartLineItemsDto, cart)).toList();

        cart.setCartLineItemsList(cartLineItems);
        cartRepository.save(cart);
    }

    private CartLineItems mapToDto(CartLineItemsDto cartLineItemsDto, Cart cart) {
        CartLineItems cartLineItems = new CartLineItems();
        cartLineItems.setCart(cart);
        cartLineItems.setQuantity(cartLineItemsDto.getQuantity());
        cartLineItems.setBookId(cartLineItemsDto.getBookId());
        cartLineItems.setPrice(cartLineItemsDto.getPrice());
        return cartLineItems;
    }
}
