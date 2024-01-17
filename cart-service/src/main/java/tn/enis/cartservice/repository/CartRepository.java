package tn.enis.cartservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.cartservice.model.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findCartsByUserId(Long userId);
}
