package tn.enis.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
