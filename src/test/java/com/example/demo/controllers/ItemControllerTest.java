package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo = mock(ItemRepository.class);

    private List<Item> itemList;
    private long item1Id;
    private String item1Name;

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
        itemList = new ArrayList<>();

            Item item1 = new Item();
            item1.setName("test1");
            item1.setDescription("testItem1");
            item1.setPrice(BigDecimal.ONE);
            item1.setId(Long.MAX_VALUE);
            itemList.add(item1);


            Item item2 = new Item();
            item2.setName("test2");
            item2.setDescription("testItem2");
            item2.setPrice(BigDecimal.ONE);
            itemList.add(item2);

            item1Id = item1.getId();
            item1Name = item1.getName();


    }

    @Test
    public void get_items_test(){
        when(itemRepo.findAll()).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(itemList.size(), response.getBody().size());
        assertEquals(itemList.get(0), response.getBody().get(0));
    }

    @Test
    public void get_item_by_id_exists(){
        when(itemRepo.findById(item1Id)).thenReturn(Optional.of(itemList.get(0)));

        ResponseEntity<Item> response = itemController.getItemById(item1Id);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item1Name, response.getBody().getName());

    }

    @Test
    public void get_items_by_name_exists(){
        String testItemName = itemList.get(0).getName();
        when(itemRepo.findByName(testItemName)).thenReturn(Arrays.asList(itemList.get(0)));

        ResponseEntity<List<Item>> response = itemController.getItemsByName(testItemName);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }


}
