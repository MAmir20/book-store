package tn.enis.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.enis.userservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
