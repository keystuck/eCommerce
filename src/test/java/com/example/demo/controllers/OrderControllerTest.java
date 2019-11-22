package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
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

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepo = mock(UserRepository.class);

    private String testUserName = "testUserName";

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
    }

    @Test
    public void submit_order_test_exists_empty_cart(){
        User testUser = new User();
        testUser.setUsername("testUserName");

        when(userRepo.findByUsername(testUserName)).thenReturn(Optional.of(testUser));

        ResponseEntity<UserOrder> response = orderController.submit(testUserName);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

}
