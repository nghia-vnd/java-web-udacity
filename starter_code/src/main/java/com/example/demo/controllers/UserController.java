package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

	final
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, CartRepository cartRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        log.debug("Start process find user by ID: {}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        log.debug("Start process find user with UserName: {}", username);
        User user = userRepository.findByUsername(username);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest createUserRequest, BindingResult bindingResult) {
        log.debug("Start process create user with UserName: {}", createUserRequest.getUsername());
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            log.error("Failed to create new user with error: {}", errorMessages);
            return ResponseEntity.badRequest().body(errorMessages);
        } else if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
            log.error("Failed to create new user with error: Password and Confirm password do not match");
            return ResponseEntity.badRequest().body("Both passwords must match.");
        }else if (userRepository.findByUsername(createUserRequest.getUsername()) != null){
            log.error("Failed to create new user with error: Username already exists");
            return ResponseEntity.badRequest().body("Username already exists.");
        }
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
        log.debug("End process create new user: {}", createUserRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
