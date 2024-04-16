package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;
  private final CookbookService cookbookService;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;

  UserController(UserService userService, CookbookService cookbookService, UserRepository userRepository, GroupRepository groupRepository) {
    this.userService = userService;
    this.cookbookService = cookbookService;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
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

    @PostMapping("/users/{userID}/accept/{groupID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void acceptInvitation(@PathVariable("userID") Long userID, @PathVariable("groupID") Long groupID) {
    
        User user = userRepository.findById(userID).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        List<Long> invitations = user.getInvitations();
        if (invitations.contains(groupID)) {
            invitations.remove(groupID); // Remove the groupID from the list -> accept
            userRepository.save(user);
            userRepository.flush();
        } else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found");
        }

        Group group = groupRepository.findById(groupID).orElse(null);
        if (group == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }

        List<Long> members = group.getMembers();
        if (!members.contains(userID)){
          members.add(userID);
          group.setMembers(members);
          groupRepository.save(group);
          groupRepository.flush();
        } else {
          throw new ResponseStatusException(HttpStatus.CONFLICT);
        }        
    }

    @PostMapping("/users/{userID}/deny/{groupID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void declineInvitation(@PathVariable("userID") Long userID, @PathVariable("groupID") Long groupID) {
    
        User user = userRepository.findById(userID).orElse(null);
        if (user == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        
        List<Long> invitations = user.getInvitations();
        if (invitations.contains(groupID)) {
          invitations.remove(groupID); // Remove the groupID from the list -> decline
          userRepository.save(user);
          userRepository.flush();
        } else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found");
        }
    }

    @GetMapping("/users/{userID}/invitations")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Long> getAllInvitations(@PathVariable("userID") Long userID) {

      User user = userRepository.findById(userID).orElse(null);
      if (user == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }

      return user.getInvitations();
    }
    
}
