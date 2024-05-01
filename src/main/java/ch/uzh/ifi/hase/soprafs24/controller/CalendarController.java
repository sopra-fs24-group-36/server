package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.CalendarRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ItemRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.DateRecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CalendarService;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
public class CalendarController {

  private final CalendarService calendarService;
  private final UserRepository userRepository;
  private final GroupRepository groupRepository;

  CalendarController(CalendarService calendarService, UserRepository userRepository, GroupRepository groupRepository) {this.calendarService = calendarService; this.userRepository = userRepository; this.groupRepository = groupRepository;}

  @PostMapping("/users/{userId}/calendars")
  @ResponseStatus(HttpStatus.OK)
  public void addRecipeToCalendar(@RequestBody DateRecipeDTO dateRecipePostDTO, @PathVariable("userId") Long userId) {
    
    CalendarRequest calendarRequest = DTOMapper.INSTANCE.convertDateRecipePostDTOtoEntity(dateRecipePostDTO);

    User user = userRepository.findById(userId).orElse(null);

    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }

    Long recipeID = calendarRequest.getRecipeID();

    Date date = calendarRequest.getDate();

    calendarService.addRecipeToUserCalendar(user, recipeID, date);
  }

  @PostMapping("/groups/{groupId}/calendars")
  @ResponseStatus(HttpStatus.OK)
  public void addRecipeToGroupCalendar(@RequestBody DateRecipeDTO dateRecipePostDTO, @PathVariable("groupId") Long groupId) {
    
    CalendarRequest calendarRequest = DTOMapper.INSTANCE.convertDateRecipePostDTOtoEntity(dateRecipePostDTO);

    Group group = groupRepository.findById(groupId).orElse(null);

    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }

    Long recipeID = calendarRequest.getRecipeID();

    Date date = calendarRequest.getDate();

    calendarService.addRecipeToGroupCalendar(group, recipeID, date);
  }

  @DeleteMapping("/users/{userId}/calendars")
  @ResponseStatus(HttpStatus.OK)
  public void removeRecipeFromUserCalendar(@RequestBody DateRecipeDTO dateRecipePostDTO, @PathVariable("userId") Long userId) {
    
    CalendarRequest calendarRequest = DTOMapper.INSTANCE.convertDateRecipePostDTOtoEntity(dateRecipePostDTO);

    User user = userRepository.findById(userId).orElse(null);

    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }

    Long recipeID = calendarRequest.getRecipeID();

    Date date = calendarRequest.getDate();

    calendarService.removeRecipeFromUserCalendar(user, recipeID, date);
  }

  @DeleteMapping("/groups/{groupId}/calendars")
  @ResponseStatus(HttpStatus.OK)
  public void removeRecipeFromGroupCalendar(@RequestBody DateRecipeDTO dateRecipePostDTO, @PathVariable("groupId") Long groupId) {
    
    CalendarRequest calendarRequest = DTOMapper.INSTANCE.convertDateRecipePostDTOtoEntity(dateRecipePostDTO);

    Group group = groupRepository.findById(groupId).orElse(null);

    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }

    Long recipeID = calendarRequest.getRecipeID();

    Date date = calendarRequest.getDate();

    calendarService.removeRecipeFromGroupCalendar(group, recipeID, date);
  }
  

}
