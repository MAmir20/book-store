package tn.enis.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.enis.userservice.model.Order;
import tn.enis.userservice.repository.OrderRepository;

import java.util.List;

@SpringBootApplication
public class OrderServiceApplication implements CommandLineRunner {
	@Autowired
	private OrderRepository orderRepository;

	public static void main(String[] args) {

		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//orderRepository.save(new Order(null,new Date(),"En cours","2023-11-25"));
		List<Order> orders =orderRepository.findAll();
		orders.forEach(o->{
			System.out.println(o.toString());});
	}
}
