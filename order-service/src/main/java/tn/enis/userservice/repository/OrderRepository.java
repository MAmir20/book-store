package tn.enis.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.userservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
