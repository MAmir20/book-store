package tn.enis.orderservice.controller;

import com.fasterxml.jackson.databind.node.BooleanNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import tn.enis.orderservice.model.Order;
import tn.enis.orderservice.repository.OrderRepository;
import tn.enis.orderservice.service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping({"/",""})
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping({"/",""})
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "cart", fallbackMethod = "createOrderFallback")
//    @TimeLimiter(name = "cart")
//    @Retry(name = "cart")
    public String createOrder(@RequestBody Order order) {
        return orderService.placeOrder(order);
    }

    public String createOrderFallback(Order order, Throwable t) {
        log.error("Cannot reach Cart Service ...");
        return "Cannot reach Cart Service ...";
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {
        if (orderRepository.existsById(orderId)) {
            updatedOrder.setId(orderId);
            Order updated=orderRepository.save(updatedOrder);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderRepository.deleteById(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

