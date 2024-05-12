package tn.enis.userservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.enis.userservice.dto.UserRequest;
import tn.enis.userservice.dto.UserResponse;
import tn.enis.userservice.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping({"/",""})
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping({"/",""})
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserRequest user) {
        userService.saveUser(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable Long userId, @RequestBody UserRequest updatedUser) {
        userService.updateUser(userId, updatedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/exists")
    @ResponseStatus(HttpStatus.OK)
    public boolean userExists(@RequestParam Long userId) {
        return userService.userExists(userId);
    }


    @GetMapping("/checkCredentials")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> checkCredentials(
            @RequestParam String email,
            @RequestParam String password) {
        Long userId = userService.checkUserCredentials(email, password);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

