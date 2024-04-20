package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@Service
@Transactional
public class GroupService {

  private final Logger log = LoggerFactory.getLogger(GroupService.class);

  private final GroupRepository groupRepository;

  @Autowired
  public GroupService(@Qualifier("groupRepository") GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
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

  public Group addUserToGroup(Long userID, Long groupID){
    Optional<Group> groupOptional = groupRepository.findById(groupID);
    
    if (groupOptional.isPresent()) {
        Group group = groupOptional.get();

        List<Long> members = group.getMembers();
        members.add(userID);
        group.setMembers(members);
        groupRepository.save(group);
        groupRepository.flush();

        return group;
    } else {
        throw new RuntimeException("Group with ID " + groupID + " not found");
    }
  }

  public Group deleteUserFromGroup(Long groupId, Long userId){
    Optional<Group> groupOptional = groupRepository.findById(groupId);

    if (groupOptional.isPresent()) {
        Group group = groupOptional.get();

        List<Long> members = group.getMembers();

        if (members.contains(userId)) {
            members.remove(userId);
            group.setMembers(members);
            groupRepository.save(group);
            groupRepository.flush();

            return group;
        } else {
            throw new RuntimeException("User with ID " + userId + " is not a member of group with ID " + groupId);
        }
    } else {
        throw new RuntimeException("Group with ID " + groupId + " not found");
    }
  }
}