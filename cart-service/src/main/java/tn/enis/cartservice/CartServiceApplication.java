package tn.enis.cartservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.enis.cartservice.model.Cart;
import tn.enis.cartservice.repository.CartRepository;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class CartServiceApplication implements CommandLineRunner {
	@Autowired
	private CartRepository cartRepository;

	public static void main(String[] args) {

		SpringApplication.run(CartServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//cartRepository.save(new Cart(null,new Date(),"En cours","2023-11-25"));
		List<Cart> carts = cartRepository.findAll();
		carts.forEach(o->{
			System.out.println(o.toString());});
	}
}
