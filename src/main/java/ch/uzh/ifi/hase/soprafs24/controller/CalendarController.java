package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarOutput;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarRequest;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ItemRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CalendarDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.DateRecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CalendarService;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import org.hibernate.mapping.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;






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

    CalendarStatus status = calendarRequest.getStatus();

    calendarService.addRecipeToUserCalendar(user, recipeID, date, status);
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

    CalendarStatus status = calendarRequest.getStatus();

    calendarService.addRecipeToGroupCalendar(group, recipeID, date, status);
  }

  @DeleteMapping("/users/{userId}/calendars/{eventId}")
  @ResponseStatus(HttpStatus.OK)
  public void removeRecipeFromUserCalendar(@PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId) {
        
    User user = userRepository.findById(userId).orElse(null);

    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }

    calendarService.removeRecipeFromUserCalendar(user, eventId);
  }

  @DeleteMapping("/groups/{groupId}/calendars/{eventId}")
  @ResponseStatus(HttpStatus.OK)
  public void removeRecipeFromGroupCalendar(@PathVariable("eventId") Long eventId, @PathVariable("groupId") Long groupId) {
        
    Group group = groupRepository.findById(groupId).orElse(null);

    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }

    calendarService.removeRecipeFromGroupCalendar(group, eventId);
  }

  @GetMapping("/users/{userId}/calendars")
  @ResponseStatus(HttpStatus.OK)
  public List<CalendarOutput> getUserCalendar(@PathVariable("userId") Long userId) {
    
    User user = userRepository.findById(userId).orElse(null);

    if(user == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
    }

    List<CalendarOutput> calendarOutputs = calendarService.getUserCalendar(user);
    
    return calendarOutputs;
  }

  @GetMapping("/groups/{groupId}/calendars")
  @ResponseStatus(HttpStatus.OK)
  public List<CalendarOutput> getGroupCalendar(@PathVariable("groupId") Long groupId) {
    
    Group group = groupRepository.findById(groupId).orElse(null);

    if(group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found.");
    }

    List<CalendarOutput> calendarOutputs = calendarService.getGroupCalendar(group);
    
    return calendarOutputs;
  }
}
