package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@Service
@Transactional
public class GroupService {

  private final Logger log = LoggerFactory.getLogger(GroupService.class);

  private final GroupRepository groupRepository;
  private final UserRepository userRepository;
  private final CookbookService cookbookService;
  private final ShoppingListService shoppingListService;
  private final CalendarService calendarService;

  @Autowired
  public GroupService(@Qualifier("groupRepository") GroupRepository groupRepository, UserRepository userRepository, CookbookService cookbookService, ShoppingListService shoppingListService, CalendarService calendarService) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.cookbookService = cookbookService;
    this.shoppingListService = shoppingListService;
    this.calendarService = calendarService;
  }


  public Group createGroup(Long creator, Group newGroup) {

    User u = userRepository.findById(creator).orElse(null);
    if (u == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator with ID: "+creator+" was not found");}

      if (newGroup.getName().trim().isEmpty()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameter need to have at least length 1");
      }

    newGroup = groupRepository.save(newGroup);

    List<Long> groups = u.getGroups();
    groups.add(newGroup.getId());
    u.setGroups(groups);

    List<Long> members = new ArrayList<>();
    members.add(u.getId());
    newGroup.setMembers(members);

    List<String> membersToAdd = newGroup.getMembersNames();
    //something to add the user who created it

    for(String member:membersToAdd){
      if(userRepository.findByEmail(member)==null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found, " + member);
      }
    }

    for(String member:membersToAdd){
      User user = userRepository.findByEmail(member);
      List<Long> invitations = user.getInvitations();
      invitations.add(newGroup.getId());
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

    Calendar calendar = new Calendar();
    Calendar newCalendar = calendarService.createCalendar(calendar);
    newGroup.setCalendar(calendar);

    saveCalendar(newGroup, calendar);

    saveShoppingList(newGroup, newShoppingList);
    
    //set the ID of the cookbook to the GROUP it belongs to
    saveCookbook(newGroup, newCookbook);

    groupRepository.flush();

    log.debug("Created new Group: {}", newGroup);

    return newGroup;
  }

  public void saveCookbook (Group group, Cookbook cookbook) {
    group.setCookbook(cookbook);
  }


  public void saveShoppingList(Group group, ShoppingList shoppingList){
    group.setShoppingList(shoppingList);
  }

  public void saveCalendar(Group group, Calendar calendar){
    group.setCalendar(calendar);
  }

  public Group addUserToGroup(Long userID, Long groupID){
  
    Optional<Group> groupOptional = groupRepository.findById(groupID);
    
    if (groupOptional.isPresent()) {
        Group group = groupOptional.get();

        List<Long> members = group.getMembers();
        members.add(userID);
        group.setMembers(members);
        groupRepository.save(group);
        groupRepository.flush();


        User user = userRepository.findById(userID).orElse(null);
        if (user == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        List<Long> groups = user.getGroups();
        groups.add(groupID);
        user.setGroups(groups);
        userRepository.save(user);
        userRepository.flush();

        return group;

    } else {
        throw new RuntimeException("Group with ID " + groupID + " not found");
    }
  }

  public Group deleteUserFromGroup(Long userId, Long groupId){
    Optional<Group> groupOptional = groupRepository.findById(groupId);

    if (groupOptional.isPresent()) {
        Group group = groupOptional.get();

        List<Long> members = group.getMembers();

        if (members.contains(userId)) {
            members.remove(userId);
            group.setMembers(members);
            groupRepository.save(group);
            groupRepository.flush();


            User user = userRepository.findById(userId).orElse(null);
            if (user == null){
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            List<Long> groups = user.getGroups();
            groups.remove(groupId);
            user.setGroups(groups);
            userRepository.save(user);
            userRepository.flush();

            return group;

        } else {
            throw new RuntimeException("User with ID " + userId + " is not a member of group with ID " + groupId);
        }
    } else {
        throw new RuntimeException("Group with ID " + groupId + " not found");
    }
  }

  public void inviteUserToGroup(Long groupID, UserPostDTO userPostDTO){

    Group group = groupRepository.findById(groupID).orElse(null);
    if (group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
    }
    
    String email = userPostDTO.getEmail();

    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    //check that user not already has invitation from group and not already part of group
    if (!user.getInvitations().contains(group.getId()) && !user.getGroups().contains(group.getId())) {
        List<Long> invitations = user.getInvitations();
        invitations.add(groupID);
        user.setInvitations(invitations);

        userRepository.save(user);
        userRepository.flush();
    }

  }
}