package tn.enis.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.enis.orderservice.model.Order;
import tn.enis.orderservice.repository.OrderRepository;

import java.util.Date;
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
		orderRepository.save(new Order(null,new Date(),"Accepté","chéque",new Date()));
		List<Order> orders =orderRepository.findAll();
		orders.forEach(o->{
			System.out.println(o.toString());});
		System.out.println("****************");
		Order order =orderRepository.findById(Long.valueOf(1)).get();
		System.out.println(order.getId());
		System.out.println(order.getDate());
		System.out.println(order.getPayment());
		System.out.println(order.getStatus());
		System.out.println(order.getDeliveryDate());
		System.out.println("****************");

		List<Order> ordersByDate = orderRepository.findByDate(new Date());
		ordersByDate.forEach(o->{
			System.out.println(o.toString());});
		System.out.println("----------------");
		List<Order> ordersByDateGreaterThan = orderRepository.findByDateGreaterThan((new Date()));
		System.out.println("Orders by Date Greater Than:");
		ordersByDateGreaterThan.forEach(o->{
			System.out.println(o.toString());});
		System.out.println("----------------");
		List<Order> ordersByStatus = orderRepository.findByStatus("Refusé");
		System.out.println("Orders by Status:");
		ordersByStatus.forEach(o->{
			System.out.println(o.toString());});
		System.out.println("----------------");
		List<Order> ordersByPayement = orderRepository.findByPayment("chéque");
		System.out.println("Orders by Payment:");
		ordersByPayement.forEach(o->{
			System.out.println(o.toString());});
	}
}
