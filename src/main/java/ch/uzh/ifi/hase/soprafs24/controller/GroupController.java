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
  private final CookbookService cookbookService;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;
  private final ShoppingListService shoppingListService;

  GroupController(GroupService groupService, CookbookService cookbookService, UserRepository userRepository, GroupRepository groupRepository, ShoppingListService shoppingListService) {
    this.groupService = groupService;
    this.cookbookService = cookbookService;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
    this.shoppingListService = shoppingListService;
  }

  //here come the post/get/put mappings
  // add group
  @PostMapping("/groups")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public GroupDTO createGroup(@RequestBody GroupPostDTO groupPostDTO) {

    Long creator = groupPostDTO.getCreator();
    User u = userRepository.findById(creator).orElse(null);
    if (u == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator with ID: "+creator+" was not found");}

    Group groupInput = DTOMapper.INSTANCE.convertGroupPostDTOtoEntity(groupPostDTO);

    Group createdGroup = groupService.createGroup(groupInput);

    List<Long> groups = u.getGroups();
    groups.add(createdGroup.getId());
    u.setGroups(groups);

    List<Long> members = new ArrayList<>();
    members.add(u.getId());
    createdGroup.setMembers(members);

    List<String> membersToAdd = groupInput.getMembersNames();
    List<Long> groupMembers = createdGroup.getMembers();
    //something to add the user who created it

    for(String member:membersToAdd){
      if(userRepository.findByEmail(member)==null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found, " + member);
      }
    }

    for(String member:membersToAdd){
      User user = userRepository.findByEmail(member);
      List<Long> invitations = user.getInvitations();
      invitations.add(createdGroup.getId());
      user.setInvitations(invitations);
      userRepository.save(user);
      userRepository.flush();
    }

    //create the group cookbook as soon as a new group is created
    Cookbook cookbook = new Cookbook();
    cookbook.setStatus(CookbookStatus.GROUP);
    Cookbook newCookbook = cookbookService.createCookbook(cookbook);

    ShoppingList shoppingList = new ShoppingList();
    ShoppingList newShoppingList = shoppingListService.createShoppingList(shoppingList);

    groupService.saveShoppingList(createdGroup, newShoppingList);
    
    //set the ID of the cookbook to the GROUP it belongs to
    groupService.saveCookbook(createdGroup, newCookbook);

    groupRepository.save(createdGroup);
    groupRepository.flush();

    return DTOMapper.INSTANCE.convertEntityToGroupDTO(createdGroup);
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

    Group group = groupRepository.findById(groupID).orElse(null);
    if (group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
    }
    
    String email = userPostDTO.getEmail();

    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    
    List<Long> invitations = user.getInvitations();
    invitations.add(groupID);
    user.setInvitations(invitations);

    userRepository.save(user);
    userRepository.flush();
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
