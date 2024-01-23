package tn.enis.cartservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tn.enis.cartservice.dto.CartRequest;
import tn.enis.cartservice.dto.CartResponse;
import tn.enis.cartservice.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping({"/",""})
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
    public List<CartResponse> getCartsByUser(@PathVariable Long userId) {
        return cartService.getCartsByUser(userId);
    }

    @GetMapping({"/",""})
    @ResponseStatus(HttpStatus.OK)
    public List<CartResponse> getCarts() {
        return cartService.getCarts();
    }
}

