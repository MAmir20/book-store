package tn.enis.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.cartservice.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findCartByUserId(Long userId);
}
