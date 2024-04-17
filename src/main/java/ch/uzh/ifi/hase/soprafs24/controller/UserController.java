package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

  private final UserService userService;
  private final CookbookService cookbookService;

  UserController(UserService userService, CookbookService cookbookService) {
    this.userService = userService;
    this.cookbookService = cookbookService;
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
}
