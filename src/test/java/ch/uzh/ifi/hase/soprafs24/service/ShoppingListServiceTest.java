package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ShoppingListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @InjectMocks
    private ShoppingListService shoppingListService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createShoppingList_ValidInput_ShoppingListCreated() {
      // Given
      ShoppingList newShoppingList = new ShoppingList();
      when(shoppingListRepository.save(any())).thenReturn(newShoppingList);

      // When
      ShoppingList createdShoppingList = shoppingListService.createShoppingList(newShoppingList);

      // Then
      assertNotNull(createdShoppingList);
      assertEquals(newShoppingList, createdShoppingList);
      verify(shoppingListRepository, times(1)).save(any());
    }

    @Test
    public void addItemGroup_ItemNotInList_ItemAdded() {
      // Given
      Group group = new Group();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      shoppingList.setItems(items);
      group.setShoppingList(shoppingList);
      String item = "Apple";

      // When
      shoppingListService.addItemGroup(item, group);

      // Then
      assertTrue(shoppingList.getItems().contains(item));
      verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void addItemGroup_ItemInList_NothingAdded() {
      // Given
      Group group = new Group();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      shoppingList.setItems(items);
      group.setShoppingList(shoppingList);
      String item = "Apple";

      // When
      shoppingListService.addItemGroup(item, group);

      // Then
      assertTrue(shoppingList.getItems().contains(item));
    }

    @Test
    public void addItemUser_ItemNotInList_ItemAdded() {
      // Given
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);
      String item = "Banana";

      // When
      shoppingListService.addItemUser(item, user);

      // Then
      assertTrue(shoppingList.getItems().contains(item));
      verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void addItemUser_ItemInList_NothingAdded() {
      // Given
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Banana");
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);
      String item = "Banana";

      // When
      shoppingListService.addItemUser(item, user);

      // Then
      assertTrue(shoppingList.getItems().contains(item));
    }

    @Test
    public void removeGroupItem_ItemInList_ItemRemoved() {
      // Given
      Group group = new Group();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      items.add("Banana");
      shoppingList.setItems(items);
      group.setShoppingList(shoppingList);
      String item = "Banana";

      // When
      shoppingListService.removeGroupItem(item, group);

      // Then
      assertFalse(shoppingList.getItems().contains(item));
      verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void removeUserItem_ItemInList_ItemRemoved() {
      // Given
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      items.add("Banana");
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);
      String item = "Banana";

      // When
      shoppingListService.removeUserItem(item, user);

      // Then
      assertFalse(shoppingList.getItems().contains(item));
      verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void removeGroupItem_ItemNotInList_ResponseStatusExceptionThrown() {
      // Given
      Group group = new Group();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      shoppingList.setItems(items);
      group.setShoppingList(shoppingList);
      String item = "Banana";

      // Then
      assertThrows(ResponseStatusException.class, () -> shoppingListService.removeGroupItem(item, group));
    }

    @Test
    public void removeUserItem_ItemNotInList_ResponseStatusExceptionThrown() {
      // Given
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);
      String item = "Banana";

      // Then
      assertThrows(ResponseStatusException.class, () -> shoppingListService.removeUserItem(item, user));
    }

    @Test
    public void emptyGroupShoppinglist_ShoppingListItemsRemoved() {
      // Given
      Group group = new Group();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      items.add("Banana");
      shoppingList.setItems(items);
      group.setShoppingList(shoppingList);

      // When
      shoppingListService.emptyGroupShoppinglist(group);

      // Then
      assertTrue(shoppingList.getItems().isEmpty());
      verify(shoppingListRepository, times(1)).save(shoppingList);
    }

    @Test
    public void emptyUserShoppinglist_ShoppingListItemsRemoved() {
      // Given
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      items.add("Banana");
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);

      // When
      shoppingListService.emptyUserShoppinglist(user);

      // Then
      assertTrue(shoppingList.getItems().isEmpty());
      verify(shoppingListRepository, times(1)).save(shoppingList);
    }
}
