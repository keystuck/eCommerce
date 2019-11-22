package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private String testUserName = "testUserName";
    private String testPassword = "testPassword";


    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception {

        when(encoder.encode(testPassword)).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(testUserName);
        createUserRequest.setPassword(testPassword);
        createUserRequest.setConfirmPassword(testPassword);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(testUserName, user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void create_user_bad_password(){
        String badPassword = "bad";
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername(testUserName);
        createUserRequest.setPassword(testPassword);
        createUserRequest.setConfirmPassword(badPassword);

        final ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        createUserRequest.setPassword(badPassword);

        final ResponseEntity<User> response2 = userController.createUser(createUserRequest);
        assertNotNull(response2);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());

    }



    @Test
    public void find_user_by_name_test_exists(){
        User user = new User();
        user.setUsername(testUserName);
        user.setPassword("testPassword");
        long testUserId = user.getId();

        when(userRepo.findByUsername(testUserName)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findByUserName(testUserName);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(testUserId, response.getBody().getId());
    }

    @Test
    public void find_user_by_id_test_exists(){
        User user = new User();
        user.setUsername(testUserName);
        user.setPassword("testPassword");
        long testUserId = user.getId();

        when(userRepo.findById(testUserId)).thenReturn(Optional.of(user));


        ResponseEntity<User> response = userController.findById(testUserId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserName, response.getBody().getUsername());
    }


}

