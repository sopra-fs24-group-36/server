package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.CalendarService;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class UserController {

  private final UserService userService;
  private final CookbookService cookbookService;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;
  private final ShoppingListService shoppingListService;
  private final CalendarService calendarService;

  UserController(UserService userService, CookbookService cookbookService, UserRepository userRepository, GroupRepository groupRepository, ShoppingListService shoppingListService, CalendarService calendarService) {
    this.userService = userService;
    this.cookbookService = cookbookService;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
    this.shoppingListService = shoppingListService;
    this.calendarService = calendarService;
  }

  // add user
  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserDTO createUser(@RequestBody UserPostDTO userPostDTO) {

    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    User createdUser = userService.createUser(userInput);

    //create the personal cookbook as soon as a new user registers
    Cookbook cookbook = new Cookbook();
    cookbook.setStatus(CookbookStatus.PERSONAL);
    Cookbook newCookbook = cookbookService.createCookbook(cookbook);

    //create a new shoppinglist and save it in the user
    ShoppingList shoppingList = new ShoppingList();
    ShoppingList newShoppingList = shoppingListService.createShoppingList(shoppingList);
    userService.saveShoppingList(createdUser, newShoppingList);

    //create a new calendar and save it in the user
    Calendar calendar = new Calendar();
    Calendar newCalendar = calendarService.createCalendar(calendar);
    userService.saveCalendar(createdUser, newCalendar);

    //set the ID of the cookbook to the USER it belongs to
    userService.saveCookbook(createdUser, newCookbook);

    return DTOMapper.INSTANCE.convertEntityToUserDTO(createdUser);
  }


    // log in user: set status to ONLINE
    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO logInUser(@RequestBody UserPostDTO userPostDTO) {

        User userCredentials = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        User user = userService.logIn(userCredentials);

        return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
    }


    // log out user: set Status to OFFLINE
    @PostMapping("/users/logout/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void logOutUser(@PathVariable Long userId) {

        userService.logOut(userId);
    }

    @PostMapping("/users/{userID}/accept/{groupID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvitation(@PathVariable("userID") Long userID, @PathVariable("groupID") Long groupID) {
    
        userService.userAcceptsInvitation(userID, groupID);
    }

    @PostMapping("/users/{userID}/deny/{groupID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void declineInvitation(@PathVariable("userID") Long userID, @PathVariable("groupID") Long groupID) {

        userService.userDeclinesInvitation(userID, groupID);

    }

    @GetMapping("/users/{userID}/invitations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Map<String, Object>> getAllInvitations(@PathVariable("userID") Long userID) {

      User user = userRepository.findById(userID).orElse(null);
      if (user == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }

      List<Map<String, Object>> returnlistofmaps = new ArrayList<>();

      for (Long groupID:user.getInvitations()){
        Group g = groupRepository.findById(groupID).orElse(null);

        if (g == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }

        Map<String, Object> tuple = new HashMap<>();

        // Add elements to the map
        tuple.put("groupName", g.getName());
        tuple.put("groupImage", g.getImage());
        tuple.put("groupID", g.getId());

        returnlistofmaps.add(tuple);
      }

      return returnlistofmaps;
    }
    

    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserDTO getUser(@PathVariable Long userId) {

      User user = userService.getTheUser(userId);

      return DTOMapper.INSTANCE.convertEntityToUserDTO(user);

    }


    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUser (@PathVariable Long userId, @RequestBody UserPutDTO userPutDTO) {

        User userUpdate = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        userService.updateTheUser(userId, userUpdate);

    }

    @GetMapping("/users/{userID}/groups")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Map<String, Object>> getUserGroups(@PathVariable("userID") Long userID) {

      List<Map<String, Object>> returnlistofmaps = new ArrayList<>();
      
      User user = userRepository.findById(userID).orElse(null);
      if (user == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }
      List<Long> groups = user.getGroups();

      for(Long groupID: groups){
        Group group = groupRepository.findById(groupID).orElse(null);
        if (group == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }

        Map<String, Object> tuple = new HashMap<>();
        tuple.put("groupName", group.getName());
        tuple.put("groupImage", group.getImage());
        tuple.put("groupID", group.getId());

        returnlistofmaps.add(tuple);
      }
      return returnlistofmaps;
    }
    
}
