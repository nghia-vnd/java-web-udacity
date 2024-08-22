package com.example.demo.test;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("NGUYEN PHUOC NGHIA");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("NGUYEN PHUOC NGHIA", Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    public void testFindByUserName() {
        User user = new User();
        user.setUsername("NGUYEN PHUOC NGHIA");

        when(userRepository.findByUsername("NGUYEN PHUOC NGHIA")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("NGUYEN PHUOC NGHIA");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("NGUYEN PHUOC NGHIA", Objects.requireNonNull(response.getBody()).getUsername());
    }

    @Test
    public void testCreateUser_ValidRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("NGUYEN PHUOC NGHIA");
        request.setPassword("password123");
        request.setConfirmPassword("password123");

        User user = new User();
        user.setUsername("NGUYEN PHUOC NGHIA");
        Cart cart = new Cart();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        ResponseEntity<?> response = userController.createUser(request, bindingResult);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_InvalidRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("NGUYEN PHUOC NGHIA");
        request.setPassword("password123");
        request.setConfirmPassword("password");

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = userController.createUser(request, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateUser_ValidationErrors() {
        CreateUserRequest request = new CreateUserRequest();

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
                Collections.singletonList(new ObjectError("username", "Username is required"))
        );
        ResponseEntity<?> response = userController.createUser(request, bindingResult);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

