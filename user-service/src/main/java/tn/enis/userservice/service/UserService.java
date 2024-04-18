package tn.enis.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.enis.userservice.dto.UserRequest;
import tn.enis.userservice.dto.UserResponse;
import tn.enis.userservice.model.User;
import tn.enis.userservice.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private  final UserRepository userRepository;

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        log.info("All users are retrieved");
        return users.stream().map(this::mapToUserResponse).toList();
    }
    public void saveUser(UserRequest userRequest){
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
        userRepository.save(user);
        log.info("User {} is saved", user.getId());
    }

    public void updateUser(Long userId, UserRequest userRequest){
        User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
        user.setId(userId);
        userRepository.save(user);
        log.info("User {} is updated", user.getId());
    }
    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
        log.info("User {} is deleted", userId);
    }

    public UserResponse getUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        log.info("User {} is retrieved", userId);
        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @SneakyThrows
    public boolean userExists(Long userId) {
//        log.info("Wait started");
//        Thread.sleep(10000);
//        log.info("Wait ended");
        return userRepository.existsById(userId);
    }
    public Long checkUserCredentials(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.info("User with email {} does not exist", email);
            return null;
        } else {
            if (user.getPassword().equals(password)) {
                log.info("User with email {} has provided correct password", email);
                return user.getId();
            } else {
                log.info("User with email {} has provided incorrect password", email);
                return null;
            }
        }
    }
}
