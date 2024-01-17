package tn.enis.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.enis.cartservice.dto.CartRequest;
import tn.enis.cartservice.dto.CartResponse;
import tn.enis.cartservice.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String newCart(@RequestBody CartRequest cartRequest) {
        if(cartService.newCart(cartRequest)){
            return "Cart created successfully";
        } else {
            return "Error creating cart";
        }
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }
}

