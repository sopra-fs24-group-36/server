package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupController {

  private final GroupService groupService;

  GroupController(GroupService groupService) {this.groupService = groupService; }

  //here come the post/get/put mappings
}
