package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;
import ch.uzh.ifi.hase.soprafs24.service.GroupService;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class GroupController {

  private final GroupService groupService;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;

  GroupController(GroupService groupService, UserRepository userRepository, GroupRepository groupRepository) {
    this.groupService = groupService;
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

    Long creator = groupPostDTO.getCreator();

    try{
      Group createdGroup = groupService.createGroup(creator, groupInput);
      return DTOMapper.INSTANCE.convertEntityToGroupDTO(createdGroup);
    } catch (Exception e){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Creator not found or User in List not found!");
    }
  }

  // add user
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
      groupService.addUserToGroup(userID, groupID);
  
      // Convert and return the user DTO
      User user = userOptional.get();
      return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
  }

  // delete user
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
      groupService.deleteUserFromGroup(userID, groupID);
  
  }

  @PostMapping("/groups/{groupID}/invitations")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void inviteUserToGroup(@PathVariable("groupID") Long groupID, @RequestBody UserPostDTO userPostDTO) {

    groupService.inviteUserToGroup(groupID, userPostDTO);

  }

  @GetMapping("/groups/{groupID}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public GroupDTO getGroup(@PathVariable("groupID") Long groupID) {
    Group group = groupRepository.findById(groupID).orElse(null);

    if (group == null){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group with ID: " + groupID + " was not found");}

    return DTOMapper.INSTANCE.convertEntityToGroupDTO(group);
  }

  @GetMapping("/RR")
  public void getRickRolled() {
    throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Never gonna give you up, never gonna let you down!");
  }
}
