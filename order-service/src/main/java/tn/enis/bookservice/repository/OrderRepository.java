package tn.enis.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.bookservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
