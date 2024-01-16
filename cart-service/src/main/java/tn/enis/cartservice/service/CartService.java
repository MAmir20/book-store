package tn.enis.cartservice.service;

import com.fasterxml.jackson.databind.node.BooleanNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tn.enis.cartservice.dto.BookResponse;
import tn.enis.cartservice.dto.CartLineItemsDto;
import tn.enis.cartservice.dto.CartRequest;
import tn.enis.cartservice.dto.CartResponse;
import tn.enis.cartservice.model.Cart;
import tn.enis.cartservice.model.CartLineItems;
import tn.enis.cartservice.repository.CartRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private  final CartRepository cartRepository;
    private final WebClient webClient;
    public void newCart(CartRequest cartRequest) {
        Cart cart = new Cart();
        cart.setUserId(cartRequest.getUserId());
        cart.setDate(cartRequest.getDate());
        cart.setStatus(0L);

        List<CartLineItems> cartLineItems = cartRequest.getCartLineItemsDtoList().stream().map(cartLineItemsDto -> mapToDto(cartLineItemsDto, cart)).toList();

        cart.setCartLineItemsList(cartLineItems);
        List<Long> ids = cart.getCartLineItemsList().stream().map(CartLineItems::getBookId).toList();

        // Call Book Service to check availability and get the price of the book
        BookResponse[] bookResponseArray = webClient.get()
                .uri("http://localhost:8084/api/books/exists",
                        uriBuilder -> uriBuilder
                        .queryParam("bookId", ids)
                        .build())
                .retrieve()
                .bodyToMono(BookResponse[].class)
                .block();
        boolean allBooksInStock = Arrays.stream(bookResponseArray).allMatch(BookResponse::isInStock);
        if(allBooksInStock){
            cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Book not in stock");
        }
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
