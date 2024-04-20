package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ItemRequest;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Optional;

@WebMvcTest(ShoppingListController.class)
public class ShoppingListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingListService shoppingListService;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void addItemGroup_ValidInput_Success() throws Exception {
        // Given
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setItem("Test Item");

        Group group = new Group();
        group.setId(1L);

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        // Perform request
        mockMvc.perform(MockMvcRequestBuilders.post("/groups/1/shoppinglists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPostDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the service method was called
        Mockito.verify(shoppingListService).addItemGroup("Test Item", group);
    }

    @Test
    public void getGroupShoppinglist_ValidInput_Success() throws Exception {
        // Given
        Long groupId = 1L;
        Group group = new Group();
        ShoppingList shoppingList = new ShoppingList();
        // Set up your ShoppingList object
        
        group.setShoppingList(shoppingList);
        given(groupRepository.findById(groupId)).willReturn(java.util.Optional.of(group));

        // When/Then
        mockMvc.perform(get("/groups/" + groupId + "/shoppinglists")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeItemGroup_Success() throws Exception {
        // Given
        Long groupId = 1L;
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setItem("ItemToRemove");

        Group group = new Group();
        group.setId(groupId);

        // Mock repository behavior
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        doNothing().when(shoppingListService).removeGroupItem(Mockito.anyString(), any(Group.class));

        // Perform request
        mockMvc.perform(MockMvcRequestBuilders.put("/groups/{groupID}/shoppinglists", groupId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPutDTO)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the shoppingListService.removeGroupItem method was called with the correct arguments
        verify(shoppingListService).removeGroupItem("ItemToRemove", group);
    }

    @Test
    public void removeItemGroup_GroupNotFound() throws Exception {
      // Given
      Long groupId = 1L;
      ItemPutDTO itemPutDTO = new ItemPutDTO();
      itemPutDTO.setItem("ItemToRemove");

      // Mock repository behavior to return null
      when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

      // Perform request and expect 404 Not Found
      mockMvc.perform(MockMvcRequestBuilders.put("/groups/{groupID}/shoppinglists", groupId)
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(itemPutDTO)))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    public void emptyGroupShoppinglist_Success() throws Exception {
      // Given
      Long groupId = 1L;

      Group group = new Group();
      group.setId(groupId);

      // Mock repository behavior
      when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
      doNothing().when(shoppingListService).emptyGroupShoppinglist(any(Group.class));

      // Perform request
      mockMvc.perform(MockMvcRequestBuilders.delete("/groups/{groupID}/shoppinglists", groupId)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(MockMvcResultMatchers.status().isNoContent());

      verify(shoppingListService).emptyGroupShoppinglist(group);
    }

    @Test
    public void emptyGroupShoppinglist_GroupNotFound() throws Exception {
        // Given
        Long groupId = 1L;

        // Mock repository behavior to return null
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        // Perform request and expect 404 Not Found
        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/{groupID}/shoppinglists", groupId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    public void addItemUser_ValidInput_Success() throws Exception {
        // Given

        Long userID = 1L;
        ItemPostDTO itemPostDTO = new ItemPostDTO();
        itemPostDTO.setItem("Test Item");
    
        User user = new User();
    
        // Mock UserRepository behavior to return the user when findById is called with 1L
        given(userRepository.findById(userID)).willReturn(java.util.Optional.of(user));
    
        // Perform request
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/shoppinglists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPostDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    
        verify(shoppingListService).addItemUser("Test Item", user);
    }

    @Test
    public void getUserShoppinglist_ValidInput_Success() throws Exception {
        // Given
        Long userId = 1L;
        User user = new User();
        ShoppingList shoppingList = new ShoppingList();
        // Set up your ShoppingList object
        
        user.setShoppingList(shoppingList);
        given(userRepository.findById(userId)).willReturn(java.util.Optional.of(user));

        // When/Then
        mockMvc.perform(get("/users/" + userId + "/shoppinglists")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeItemUsers_Success() throws Exception {
        // Given
        Long userId = 1L;
        ItemPutDTO itemPutDTO = new ItemPutDTO();
        itemPutDTO.setItem("ItemToRemove");

        User user = new User();
        user.setId(userId);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(shoppingListService).removeUserItem(any(String.class), any(User.class));

        // Perform request
        mockMvc.perform(put("/users/{userID}/shoppinglists", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(itemPutDTO)))
                .andExpect(status().isNoContent());

        // Verify that the shoppingListService.removeUserItem method was called with the correct arguments
        verify(shoppingListService).removeUserItem("ItemToRemove", user);
    }

    @Test
    public void emptyUserShoppinglist_Success() throws Exception {
        // Given
        Long userId = 1L;

        User user = new User();
        user.setId(userId);

        // Mock repository behavior
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(shoppingListService).emptyUserShoppinglist(any(User.class));

        // Perform request
        mockMvc.perform(delete("/users/{userID}/shoppinglists", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(shoppingListService).emptyUserShoppinglist(user);
    }

    // Additional tests for other endpoints can be added similarly

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
