package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@Service
@Transactional
public class GroupService {

  private final Logger log = LoggerFactory.getLogger(GroupService.class);

  private final GroupRepository groupRepository;
  private final UserRepository userRepository;

  @Autowired
  public GroupService(@Qualifier("groupRepository") GroupRepository groupRepository, UserRepository userRepository) {
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
  }


  public Group createGroup(Group newGroup) {

    newGroup = groupRepository.save(newGroup);
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

  public void addUserToGroup(Long userID, Long groupID){
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
    } else {
        throw new RuntimeException("Group with ID " + groupID + " not found");
    }
  }

  public void deleteUserFromGroup(Long userID, Long groupID){
    Optional<Group> groupOptional = groupRepository.findById(groupID);
      
    if (groupOptional.isPresent()) {
        Group group = groupOptional.get();
        
        List<Long> members = group.getMembers();
        
        if (members.contains(userID)) {
            members.remove(userID);
            group.setMembers(members);
            groupRepository.save(group);

            User user = userRepository.findById(userID).orElse(null);
            if (user == null){
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            List<Long> groups = user.getGroups();
            groups.remove(groupID);
            user.setGroups(groups);
            userRepository.save(user);
            userRepository.flush();
        } else {
            throw new RuntimeException("User with ID " + userID + " is not a member of group with ID " + groupID);
        }
    } else {
        throw new RuntimeException("Group with ID " + groupID + " not found");
    }
  }
}