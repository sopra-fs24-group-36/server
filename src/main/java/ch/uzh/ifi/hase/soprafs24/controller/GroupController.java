package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.GroupService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class GroupController {

  private final GroupService groupService;
  private final CookbookService cookbookService;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;

  GroupController(GroupService groupService, CookbookService cookbookService, UserRepository userRepository, GroupRepository groupRepository) {
    this.groupService = groupService;
    this.cookbookService = cookbookService;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
  }

  //here come the post/get/put mappings
  // add group
  @PostMapping("/groups")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public GroupDTO createGroup(@RequestBody GroupPostDTO groupPostDTO) {

    Group groupInput = DTOMapper.INSTANCE.convertGroupPostDTOtoEntity(groupPostDTO);

    Group createdGroup = groupService.createGroup(groupInput);

    //create the personal cookbook as soon as a new user registers
    Cookbook cookbook = new Cookbook();
    cookbook.setStatus(CookbookStatus.GROUP);
    Cookbook newCookbook = cookbookService.createCookbook(cookbook);
    
    //set the ID of the cookbook to the GROUP it belongs to
    groupService.saveCookbook(createdGroup, newCookbook);

    return DTOMapper.INSTANCE.convertEntityToGroupDTO(createdGroup);
  }

  // add group
  @PostMapping("/groups/{groupID}/{userID}")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserDTO addUserToGroup(@PathVariable Long groupID, @PathVariable long userID) {
      // Check if the user exists
      Optional<User> userOptional = userRepository.findById(userID);
      if (!userOptional.isPresent()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }
  
      // If the user exists, proceed with adding them to the group
      groupService.addUserToGroup(groupID, userID);
  
      // Convert and return the user DTO
      User user = userOptional.get();
      return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
  }

  // add group
  @DeleteMapping("/groups/{groupID}/{userID}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void deleteUserFromGroup(@PathVariable Long groupID, @PathVariable long userID) {
      // Check if the user exists
      Optional<User> userOptional = userRepository.findById(userID);
      if (!userOptional.isPresent()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }
  
      // If the user exists, proceed with adding them to the group
      groupService.deleteUserFromGroup(groupID, userID);
  
  }

  @PostMapping("/groups/{groupID}/invitations")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void inviteUserToGroup(@PathVariable("groupID") Long groupID, @RequestBody String email) {
    
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    
    List<Long> invitations = new ArrayList<>();
    invitations.add(groupID);
    user.setInvitations(invitations);
    userRepository.save(user);
    userRepository.flush();
  }

}
