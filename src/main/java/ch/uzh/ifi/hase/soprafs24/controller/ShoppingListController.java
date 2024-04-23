package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ItemRequest;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ItemPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.ShoppingListDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class ShoppingListController {

  private final ShoppingListService shoppingListService;
  private final GroupRepository groupRepository;
  private final UserRepository userRepository;

  ShoppingListController(ShoppingListService shoppingListService, GroupRepository groupRepository, UserRepository userRepository) {this.shoppingListService = shoppingListService; this.groupRepository = groupRepository; this.userRepository = userRepository;}

  //here come the post/get/put mapping
  @PostMapping("/groups/{groupID}/shoppinglists")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void addItemGroup(@RequestBody ItemPostDTO itemPostDTO, @PathVariable("groupID") Long groupID) {

    ItemRequest item = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);
    String itemName = item.getItem();

    Group group = groupRepository.findById(groupID).orElse(null);
    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }
    shoppingListService.addItemGroup(itemName, group);
  }

  @PostMapping("/users/{userID}/shoppinglists")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void addItemUser(@RequestBody ItemPostDTO itemPostDTO, @PathVariable("userID") Long userID) {

    ItemRequest item = DTOMapper.INSTANCE.convertItemPostDTOtoEntity(itemPostDTO);
    String itemName = item.getItem();

    User user = userRepository.findById(userID).orElse(null);
    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }

    shoppingListService.addItemUser(itemName, user);
  }

  @GetMapping("/groups/{groupID}/shoppinglists")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ShoppingListDTO getGroupShoppinglist(@PathVariable("groupID") Long groupID) {

    Group group = groupRepository.findById(groupID).orElse(null);

    ShoppingList shoppingList = group.getShoppingList();

    return DTOMapper.INSTANCE.convertEntityToShoppingListDTO(shoppingList);
  }

  @GetMapping("/users/{userID}/shoppinglists")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ShoppingListDTO getUserShoppinglist(@PathVariable("userID") Long userID) {

    User user = userRepository.findById(userID).orElse(null);

    if (user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    ShoppingList shoppingList = user.getShoppingList();

    return DTOMapper.INSTANCE.convertEntityToShoppingListDTO(shoppingList);
  }

  @PutMapping("/groups/{groupID}/shoppinglists")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void removeItemGroup(@PathVariable("groupID") Long groupID, @RequestBody ItemPutDTO itemPutDTO) {
      
    ItemRequest item = DTOMapper.INSTANCE.convertItemPutDTOtoEntity(itemPutDTO);
    String itemName = item.getItem();

    Group group = groupRepository.findById(groupID).orElse(null);
    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }
    shoppingListService.removeGroupItem(itemName, group);
  }

  @PutMapping("/users/{userID}/shoppinglists")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void removeItemUsers(@PathVariable("userID") Long userID, @RequestBody ItemPutDTO itemPutDTO) {
      
    ItemRequest item = DTOMapper.INSTANCE.convertItemPutDTOtoEntity(itemPutDTO);
    String itemName = item.getItem();

    User user = userRepository.findById(userID).orElse(null);
    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }
    shoppingListService.removeUserItem(itemName, user);
  }

  @DeleteMapping("/groups/{groupID}/shoppinglists")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void emptyGroupShoppinglist(@PathVariable("groupID") Long groupID){

    Group group = groupRepository.findById(groupID).orElse(null);
    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }
    shoppingListService.emptyGroupShoppinglist(group);
  }

  @DeleteMapping("/users/{userID}/shoppinglists")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void emptyUserShoppinglist(@PathVariable("userID") Long userID){

    User user = userRepository.findById(userID).orElse(null);
    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }
    shoppingListService.emptyUserShoppinglist(user);
  }
  
}
