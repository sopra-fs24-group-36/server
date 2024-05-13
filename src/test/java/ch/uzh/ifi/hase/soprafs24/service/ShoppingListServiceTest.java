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
    public void addItemGroup_ItemInList_throwsException() throws Exception {
      // Given
      Group group = new Group();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      shoppingList.setItems(items);
      group.setShoppingList(shoppingList);
      String item = "Apple";

      // When
      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shoppingListService.addItemGroup(item, group));

      // Then
      assertEquals(HttpStatus.CONFLICT, exception.getStatus());
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
    public void addItemUser_ItemInList_throwsException() throws Exception {
      // Given
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Banana");
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);
      String item = "Banana";

      // When
      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> shoppingListService.addItemUser(item, user));

      // Then
      assertEquals(HttpStatus.CONFLICT, exception.getStatus());
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
      // Create a User with a shoppinglist containing Items "Apple" and "Banana"
      User user = new User();
      ShoppingList shoppingList = new ShoppingList();
      List<String> items = new ArrayList<>();
      items.add("Apple");
      items.add("Banana");
      shoppingList.setItems(items);
      user.setShoppingList(shoppingList);

      // Item which will be removed from the shoppinglist
      String item = "Banana";

      // Item which will still be in the shoppinglist
      String itemNotRemoved = "Apple";

      // Action of removing from shoppinglist
      shoppingListService.removeUserItem(item, user);

      // Checking that item is not in the shoppinglist anymore
      assertFalse(shoppingList.getItems().contains(item));

      // Checking that the other item is still in the shoppinglist
      assertTrue(shoppingList.getItems().contains(itemNotRemoved));

      // Verify that the saving of a shoppinglist is only done once during execution
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
