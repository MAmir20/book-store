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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final WebClient webClient;
    public boolean newCart(CartRequest cartRequest) {
        // Check if user exists
        boolean userExists = webClient.get()
                .uri("http://localhost:8083/api/users/exists",
                        uriBuilder -> uriBuilder
                                .queryParam("userId", cartRequest.getUserId())
                                .build())
                .retrieve()
                .bodyToMono(BooleanNode.class)
                .block()
                .asBoolean();
        if(!userExists) {
            log.error("User {} does not exist", cartRequest.getUserId());
            return false;
        }

        // Check if all books are in stock
        Map<Long, Integer> books = cartRequest.getCartLineItemsDtoList().stream().collect(Collectors.toMap(CartLineItemsDto::getBookId, CartLineItemsDto::getQuantity));
        BookResponse[] bookResponseArray = webClient.post()
                .uri("http://localhost:8084/api/books/exists")
                .bodyValue(books)
                .retrieve()
                .bodyToMono(BookResponse[].class)
                .block();
        boolean allBooksInStock = Arrays.stream(bookResponseArray).allMatch(BookResponse::isInStock)
                && bookResponseArray.length == books.size();
        if(!allBooksInStock) {
            log.error("Some books are not in stock");
            return false;
        }
        // Create a new cart
        Cart cart = new Cart();
        cart.setUserId(cartRequest.getUserId());
        cart.setDate(cartRequest.getDate());
        cart.setStatus(0L);
        List<CartLineItems> cartLineItems = cartRequest.getCartLineItemsDtoList().stream().map(cartLineItemsDto -> mapToDto(cartLineItemsDto, cart)).toList();
        cart.setCartLineItemsList(cartLineItems);
//        boolean result = webClient.put()
//                .uri("http://localhost:8084/api/books/reduceQty")
//                .bodyValue(books)
//                .retrieve()
//                .bodyToMono(BooleanNode.class)
//                .block()
//                .asBoolean();
        cartRepository.save(cart);
        return true;
    }

    private CartLineItems mapToDto(CartLineItemsDto cartLineItemsDto, Cart cart) {
        CartLineItems cartLineItems = new CartLineItems();
        cartLineItems.setCart(cart);
        cartLineItems.setQuantity(cartLineItemsDto.getQuantity());
        cartLineItems.setBookId(cartLineItemsDto.getBookId());
        cartLineItems.setPrice(cartLineItemsDto.getPrice());
        return cartLineItems;
    }

    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findCartByUserId(userId);
        if(cart == null) {
            log.error("Cart for user {} does not exist", userId);
            return null;
        }
        log.info("Cart {} is retrieved", cart.getId());
        return mapToCartResponse(cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .date(cart.getDate())
                .status(cart.getStatus())
                .total(cart.getTotal())
                .build();
    }
}
