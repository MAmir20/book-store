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

import java.math.BigDecimal;
import java.util.*;
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
        if (!userExists) {
            log.error("User {} does not exist", cartRequest.getUserId());
            return "User does not exist";
        }

        // Check if all books are in stock
        Map<Long, Integer> books = cartRequest.getCartLineItemsDtoList().stream()
                .collect(Collectors.toMap(CartLineItemsDto::getBookId, CartLineItemsDto::getQuantity));
        boolean allBooksInStock = checkBooksAvailability(books);
        if (!allBooksInStock) {
            log.error("Some books are not in stock");
            return "Some books are not in stock";
        }

        // Calculate total and totalPrice
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartLineItemsDto cartLineItem : cartRequest.getCartLineItemsDtoList()) {
            BigDecimal price = cartLineItem.getPrice();
            int quantity = cartLineItem.getQuantity();
            totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        // Create a new cart
        Cart cart = new Cart();
        cart.setUserId(cartRequest.getUserId());
        cart.setDate(cartRequest.getDate());
        cart.setStatus(0L);
        cart.setTotal(totalPrice); // Set total price
        List<CartLineItems> cartLineItems = cartRequest.getCartLineItemsDtoList().stream()
                .map(cartLineItemsDto -> mapToDto(cartLineItemsDto, cart))
                .toList();
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

    public void updateCart(Long cartId, CartRequest cartRequest) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart with ID " + cartId + " not found"));

        existingCart.setDate(cartRequest.getDate());
        existingCart.setUserId(cartRequest.getUserId());
        existingCart.setStatus(cartRequest.getStatus());

        List<CartLineItems> cartLineItemsList = existingCart.getCartLineItemsList();

        for (CartLineItemsDto dto : cartRequest.getCartLineItemsDtoList()) {
            // Chercher l'élément correspondant dans le panier
            Optional<CartLineItems> existingCartItemOptional = findCartItemByBookId(cartLineItemsList, dto.getBookId());

            if (existingCartItemOptional.isPresent()) {
                // Mettre à jour la quantité si l'élément existe déjà
                CartLineItems existingCartItem = existingCartItemOptional.get();
                existingCartItem.setQuantity(existingCartItem.getQuantity() + dto.getQuantity());
            } else {
                // Ajouter un nouvel élément au panier s'il n'existe pas encore
                CartLineItems newCartItem = mapToDto(dto, existingCart);
                cartLineItemsList.add(newCartItem);
            }
        }

        cartRepository.save(existingCart);
        log.info("Updated cart: {}", existingCart);
    }

    private Optional<CartLineItems> findCartItemByBookId(List<CartLineItems> cartLineItemsList, Long bookId) {
        return cartLineItemsList.stream()
                .filter(item -> item.getBookId().equals(bookId))
                .findFirst();
    }


    public void updateCartLineItemQuantity(Long cartId, CartRequest cartRequest) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart with ID " + cartId + " not found"));

        List<CartLineItems> cartLineItemsList = existingCart.getCartLineItemsList();
        BigDecimal newTotal = BigDecimal.ZERO; // Initialize new total

        // Loop through the cart items in the request
        for (CartLineItemsDto cartLineItemsDto : cartRequest.getCartLineItemsDtoList()) {
            // Find the corresponding cart line item in the existing cart
            for (CartLineItems cartLineItem : cartLineItemsList) {
                if (cartLineItem.getBookId().equals(cartLineItemsDto.getBookId())) {
                    // Update the quantity
                    cartLineItem.setQuantity(cartLineItemsDto.getQuantity());

                    // Update the total price for this item
                    BigDecimal totalPriceForItem = cartLineItemsDto.getPrice()
                            .multiply(BigDecimal.valueOf(cartLineItemsDto.getQuantity()));
                    cartLineItem.setTotalPrice(totalPriceForItem);

                    // Add the total price of this item to the new total
                    newTotal = newTotal.add(totalPriceForItem);

                    break;
                }
            }
        }

        // Update the total price of the cart
        existingCart.setTotal(newTotal);

        // Save the updated cart in the database
        cartRepository.save(existingCart);
        log.info("Updated cart: {}", existingCart);
    }


    public void removeCartItem(Long cartId, Long bookId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart with ID " + cartId + " not found"));

        List<CartLineItems> cartLineItemsList = existingCart.getCartLineItemsList();

        // Chercher l'index de l'élément à supprimer
        int indexToRemove = -1;
        for (int i = 0; i < cartLineItemsList.size(); i++) {
            if (cartLineItemsList.get(i).getBookId().equals(bookId)) {
                indexToRemove = i;
                break;
            }
        }

        // Supprimer l'élément s'il a été trouvé
        if (indexToRemove != -1) {
            cartLineItemsList.remove(indexToRemove);
        } else {
            // Gérer le cas où l'élément n'est pas trouvé
            throw new IllegalArgumentException("Item with bookId " + bookId + " not found in cart " + cartId);
        }

        // Mettre à jour le panier dans la base de données
        cartRepository.save(existingCart);
        log.info("Updated cart after removing item with bookId {}: {}", bookId, existingCart);
    }




}
