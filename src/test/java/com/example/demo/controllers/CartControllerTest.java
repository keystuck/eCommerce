package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepo = mock(UserRepository.class);
    private ItemRepository itemRepo = mock(ItemRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);

    private User testUser;
    private ModifyCartRequest modifyCartRequest;
    private Item testItem;

    private long testItemId = Long.MAX_VALUE;
    private String testUserName = "testUserName";

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);

        testUser = new User();
        testUser.setUsername(testUserName);
        testUser.setPassword("testUserPassword");
        testUser.setCart(new Cart());

        modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(testUserName);
        modifyCartRequest.setItemId(testItemId);
        modifyCartRequest.setQuantity(3);



        testItem = new Item();
        testItem.setId(testItemId);
        testItem.setName("testItem");
        testItem.setDescription("testItemDesc");
        testItem.setPrice(BigDecimal.ONE);

    }

    @Test
    public void add_to_cart_test(){
        when(userRepo.findByUsername(testUserName)).thenReturn(Optional.of(testUser));
        when(itemRepo.findById(testItemId)).thenReturn(Optional.of(testItem));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(3, responseEntity.getBody().getItems().size());

    }

}
