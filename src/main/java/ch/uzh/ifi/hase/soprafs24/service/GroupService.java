package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

}