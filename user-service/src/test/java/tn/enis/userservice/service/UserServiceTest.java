package tn.enis.userservice.service;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.enis.userservice.dto.UserRequest;
import tn.enis.userservice.dto.UserResponse;
import tn.enis.userservice.model.User;
import tn.enis.userservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void UserService_saveUser_ReturnUserDto() {
        //arrange
        User user = Mockito.mock(User.class);
        UserRequest userRequest = UserRequest.builder()
                .name("test")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        //act
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        userService.saveUser(userRequest);
        //assert
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

    }

    @Test
    public void UserService_getAllUsers_ReturnListOfUserDto() {
        //arrange
        List<User> users = Mockito.mock(List.class);
        //act
        when(userRepository.findAll()).thenReturn(users);
        List<UserResponse> savedUsers = userService.getAllUsers();
        //assert
        assertNotNull(savedUsers);
    }

    @Test
    public void UserService_updateUser_ReturnUserDto() {
        //arrange
        User user = Mockito.mock(User.class);
        UserRequest userRequest = UserRequest.builder()
                .name("test")
                .email("user1@gmail.com")
                .password("1234")
                .build();
        //act
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        userService.updateUser(1L, userRequest);
        //assert
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void UserService_deleteUser_ReturnVoid() {
        //arrange
        //act
        userService.deleteUser(1L);
        //assert
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void UserService_getUser_ReturnUserDto() {
        //arrange
        User user = Mockito.mock(User.class);
        //act
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        UserResponse userResponse = userService.getUser(1L);
        //assert
        assertNotNull(userResponse);
    }

    @Test
    public void UserService_userExists_ReturnTrue() {
        //arrange
        User user = Mockito.mock(User.class);
        //act
        when(userRepository.existsById(1L)).thenReturn(true);
        //assert
        assertTrue(userService.userExists(1L));
    }

    @Test
    public void UserService_userExists_ReturnFalse() {
        //arrange
        User user = Mockito.mock(User.class);
        //act
        when(userRepository.existsById(1L)).thenReturn(false);
        //assert
        assertFalse(userService.userExists(1L));
    }
}