package tn.enis.orderservice.service;

import com.fasterxml.jackson.databind.node.BooleanNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import tn.enis.orderservice.model.Order;
import tn.enis.orderservice.repository.OrderRepository;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(Order order){
        boolean cartUpdated = webClientBuilder.build().get()
                .uri("lb://cart-service/api/carts/validate/"+order.getCartId())
                .retrieve()
                .bodyToMono(BooleanNode.class)
                .block()
                .asBoolean();
        if(!cartUpdated) {
            log.error("ERROR WHILE PROCESSING ORDER", order.getCartId());
            return "ERROR WHILE PROCESSING ORDER";
        }
        order.setStatus("ORDERED");
        Order createdOrder = orderRepository.save(order);
        return "Order created successfully";
    }
}
