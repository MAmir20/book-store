package tn.enis.orderservice.repository;

import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.enis.orderservice.model.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByDate(Date date);
    List<Order> findByDateGreaterThan(Date date);

    List<Order> findByDelivery(Date delivery_date);
    List<Order> findByDeliveryDateGreaterThan(Date delivery_date);

    List<Order> findByStatus(String status);
    List<Order> findByPayment(String payment);

}
