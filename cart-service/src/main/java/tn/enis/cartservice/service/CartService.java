package tn.enis.cartservice.service;

import com.fasterxml.jackson.databind.node.BooleanNode;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final WebClient.Builder webClientBuilder;

    public String newCart(CartRequest cartRequest) {
        // Check if user exists
        boolean userExists = webClientBuilder.build().get()
                .uri("lb://user-service/api/users/exists",
                        uriBuilder -> uriBuilder
                                .queryParam("userId", cartRequest.getUserId())
                                .build())
                .retrieve()
                .bodyToMono(BooleanNode.class)
                .block()
                .asBoolean();
        if(!userExists) {
            log.error("User {} does not exist", cartRequest.getUserId());
            return "User does not exist";
        }

        // Check if all books are in stock
        Map<Long, Integer> books = cartRequest.getCartLineItemsDtoList().stream().collect(Collectors.toMap(CartLineItemsDto::getBookId, CartLineItemsDto::getQuantity));
        boolean allBooksInStock = checkBooksAvailability(books);
        if(!allBooksInStock) {
            log.error("Some books are not in stock");
            return "Some books are not in stock";
        }
        // Create a new cart
        Cart cart = new Cart();
        cart.setUserId(cartRequest.getUserId());
        cart.setDate(cartRequest.getDate());
        cart.setStatus(0L);
        List<CartLineItems> cartLineItems = cartRequest.getCartLineItemsDtoList().stream().map(cartLineItemsDto -> mapToDto(cartLineItemsDto, cart)).toList();
        cart.setCartLineItemsList(cartLineItems);
        cartRepository.save(cart);
        return "Cart created successfully";
    }

    private CartLineItems mapToDto(CartLineItemsDto cartLineItemsDto, Cart cart) {
        CartLineItems cartLineItems = new CartLineItems();
        cartLineItems.setCart(cart);
        cartLineItems.setQuantity(cartLineItemsDto.getQuantity());
        cartLineItems.setBookId(cartLineItemsDto.getBookId());
        cartLineItems.setPrice(cartLineItemsDto.getPrice());
        return cartLineItems;
    }

    public List<CartResponse> getCartsByUser(Long userId) {
        List<Cart> carts = cartRepository.findCartsByUserId(userId);
        return carts.stream().map(this::mapToCartResponse).toList();
    }

    @SneakyThrows
    public boolean validate(Long cartId) {
        log.info("Validating cart {}", cartId);
//        log.info("Wait started");
//        Thread.sleep(10000);
//        log.info("Wait ended");
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null) {
            log.error("Cart {} does not exist", cartId);
            return false;
        }
        Map<Long, Integer> books = cart.getCartLineItemsList().stream().collect(Collectors.toMap(CartLineItems::getBookId, CartLineItems::getQuantity));
        boolean allBooksInStock = checkBooksAvailability(books);
        if(!allBooksInStock) {
            log.error("Some books are not in stock");
            return false;
        }
        boolean result = webClientBuilder.build().put()
                .uri("lb://book-service/api/books/reduceQty")
                .bodyValue(books)
                .retrieve()
                .bodyToMono(BooleanNode.class)
                .block()
                .asBoolean();
        if(result) {
            cart.setStatus(1L);
            cartRepository.save(cart);
            log.info("Cart {} validated successfully", cartId);
            return true;
        } else {
            log.error("Error while reducing books quantity");
            return false;
        }
    }

    private CartResponse mapToCartResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .date(cart.getDate())
                .status(cart.getStatus())
                .total(cart.getTotal())
                .cartLineItemsListDto(cart.getCartLineItemsList().stream().map(this::mapToCartLineItemsDto).toList())
                .build();
    }

    private CartLineItemsDto mapToCartLineItemsDto(CartLineItems cartLineItems) {
        return CartLineItemsDto.builder()
                .bookId(cartLineItems.getBookId())
                .quantity(cartLineItems.getQuantity())
                .price(cartLineItems.getPrice())
                .build();
    }

    public List<CartResponse> getCarts() {
        return cartRepository.findAll().stream().map(this::mapToCartResponse).toList();
    }

    public boolean checkBooksAvailability(Map<Long, Integer> books){
        BookResponse[] bookResponseArray = webClientBuilder.build().post()
                .uri("lb://book-service/api/books/exists")
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
        return true;
    }
}
