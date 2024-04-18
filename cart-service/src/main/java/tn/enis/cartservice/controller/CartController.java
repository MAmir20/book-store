package tn.enis.cartservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enis.cartservice.dto.CartRequest;
import tn.enis.cartservice.dto.CartResponse;
import tn.enis.cartservice.model.Cart;
import tn.enis.cartservice.repository.CartRepository;
import tn.enis.cartservice.service.CartService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping({"/",""})
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "userAndBook", fallbackMethod = "newCartFallback")
//    @TimeLimiter(name = "userAndBook")
//    @Retry(name = "userAndBook")
    public String newCart(@RequestBody CartRequest cartRequest) {
        return cartService.newCart(cartRequest);
    }

    public String newCartFallback(CartRequest cartRequest, Throwable t) {
        log.error("Cannot reach User Service ...");
        return "Cannot reach User Service ...";
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CartResponse> getCartsByUser(@PathVariable Long userId) {
        return cartService.getCartsByUser(userId);
    }

    @GetMapping({"/",""})
    @ResponseStatus(HttpStatus.OK)
    public List<CartResponse> getCarts() {
        return cartService.getCarts();
    }

    @GetMapping("/validate/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean validate(@PathVariable Long cartId) {
        return cartService.validate(cartId);
    }


    @PutMapping("/{cartId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateCart(@PathVariable Long cartId, @RequestBody CartRequest cartRequest) {
        cartService.updateCart(cartId, cartRequest);
        return ResponseEntity.ok( cartRequest + " updated successfully");
    }

    @DeleteMapping("/{cartId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteCart(@PathVariable Long cartId) {
        if (cartRepository.existsById(cartId)) {
            cartRepository.deleteById(cartId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{cartId}/updatequantity/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> updateCartLineItemQuantity(@PathVariable Long cartId,
                                                             @RequestBody CartRequest cartRequest) {
        cartService.updateCartLineItemQuantity(cartId, cartRequest);
        return ResponseEntity.ok("Quantity updated successfully");
    }
    @DeleteMapping("/{cartId}/items/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(@PathVariable Long cartId, @PathVariable Long bookId) {
        cartService.removeCartItem(cartId, bookId);
    }





}

