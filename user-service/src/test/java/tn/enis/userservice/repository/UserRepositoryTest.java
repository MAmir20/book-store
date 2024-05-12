package tn.enis.userservice.repository;

import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tn.enis.userservice.dto.UserRequest;
import tn.enis.userservice.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @ClassRule

    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withDatabaseName("user_db")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dymDynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dymDynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void UserRepository_save_ReturnSavedUser() {
        //arrange
        User user = User.builder()
                .name("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        //act
        User savedUser = userRepository.save(user);
        //assert
        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > 0);
    }

    @Test
    public void UserRepository_findAll_ReturnAllUsers() {
        //arrange
        User user1 = User.builder()
                .name("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        User user2 = User.builder()
                .name("user2")
                .email("user2@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        //act
        List<User> users = userRepository.findAll();
        //assert
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void UserRepository_deleteById_DeleteUser() {
        //arrange
        User user = User.builder()
                .name("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        User savedUser = userRepository.save(user);

        //act
        userRepository.deleteById(savedUser.getId());

        //assert
        assertFalse(userRepository.existsById(savedUser.getId()));
    }

    @Test
    public void UserRepository_findById_ReturnUser() {
        //arrange
        User user = User.builder()
                .name("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();

        User savedUser = userRepository.save(user);

        //act
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);

        //assert
        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
    }

    @Test
    public void UserRepository_existsById_ReturnTrue() {
        //arrange
        User user = User.builder()
                .name("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();

        User savedUser = userRepository.save(user);

        //act
        boolean exists = userRepository.existsById(savedUser.getId());

        //assert
        assertTrue(exists);
    }

    @Test
    public void UserRepository_existsById_ReturnFalse() {
        //arrange
        User user = User.builder()
                .name("user1")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        //act
        boolean exists = userRepository.existsById(1L);
        //assert
        assertFalse(exists);
    }
}