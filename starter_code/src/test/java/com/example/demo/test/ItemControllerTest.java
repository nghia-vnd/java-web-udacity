package com.example.demo.test;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetItems() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("itemTest1");
        item1.setPrice(BigDecimal.valueOf(10.00));
        item1.setDescription("Description for item1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item2");
        item2.setPrice(BigDecimal.valueOf(20.00));
        item2.setDescription("Description for item2");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(item1.getName(), response.getBody().get(0).getName());
        assertEquals(item2.getName(), response.getBody().get(1).getName());
    }

    @Test
    public void testGetItemById() {
        Item item = new Item();
        item.setId(1L);
        item.setName("itemTest1");
        item.setPrice(BigDecimal.valueOf(10.00));
        item.setDescription("Description for item1");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item.getName(), Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void testGetItemById_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetItemsByName() {
        Item item = new Item();
        item.setId(1L);
        item.setName("itemTest1");
        item.setPrice(BigDecimal.valueOf(10.00));
        item.setDescription("Description for item1");

        when(itemRepository.findByName("itemTest1")).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemTest1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals(item.getName(), response.getBody().get(0).getName());
    }

    @Test
    public void testGetItemsByName_NotFound() {
        when(itemRepository.findByName("itemTest1")).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("itemTest1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

