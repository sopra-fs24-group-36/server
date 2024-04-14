package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GroupController {

  private final GroupService groupService;
  private final CookbookService cookbookService;

  GroupController(GroupService groupService, CookbookService cookbookService) {
    this.groupService = groupService;
    this.cookbookService = cookbookService;
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

}
