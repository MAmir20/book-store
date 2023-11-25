package tn.enis.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.enis.userservice.model.User;
import tn.enis.userservice.repository.UserRepository;

import java.util.List;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//userRepository.save(new User(null,new Date(),"En cours","2023-11-25"));
		List<User> users = userRepository.findAll();
		users.forEach(o->{
			System.out.println(o.toString());});
	}
}
