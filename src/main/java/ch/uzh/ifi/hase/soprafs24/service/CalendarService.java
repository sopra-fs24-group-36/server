package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarOutput;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CalendarRepository;
import ch.uzh.ifi.hase.soprafs24.repository.DateRecipeRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;


// Service to handle all calendar related functionality
@Service
@Transactional
public class CalendarService {

  private final Logger log = LoggerFactory.getLogger(ShoppingListService.class);

  private final CalendarRepository calendarRepository;

  private final RecipeRepository recipeRepository;

  private final DateRecipeRepository dateRecipeRepository;

  @Autowired
  public CalendarService(@Qualifier("calendarRepository") CalendarRepository calendarRepository, RecipeRepository recipeRepository, DateRecipeRepository dateRecipeRepository) {
    this.calendarRepository = calendarRepository;
    this.recipeRepository = recipeRepository;
    this.dateRecipeRepository = dateRecipeRepository;
  }

  // Create a new calendar
  public Calendar createCalendar(Calendar calendar) {

    calendar = calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Created new Calendar: {}", calendar);

    return calendar;
  }

  // Add a recipe to the calendar of a user
  public Calendar addRecipeToUserCalendar(User user, Long recipeID, Date date, CalendarStatus status) {

    Calendar calendar = user.getCalendar();

    List<DateRecipe> recipes = calendar.getDateRecipes();

    Recipe recipe = recipeRepository.findById(recipeID).orElse(null);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
    }

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(date);
    dateRecipe.setRecipeID(recipeID);
    dateRecipe.setCalendar(calendar);
    dateRecipe.setStatus(status);

    if (dateRecipeRepository.findByDateAndRecipeIDAndCalendarAndStatus(date, recipeID, calendar, status).size() > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Recipe already exists in Calendar.");
    }

    dateRecipe = dateRecipeRepository.save(dateRecipe);
    recipes.add(dateRecipe);

    calendar = calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Added recipe to Calendar: {}", calendar);

    return calendar;
  }

  // Add a recipe to the calendar of a group
  public Calendar addRecipeToGroupCalendar(Group group, Long recipeID, Date date, CalendarStatus status) {

    Calendar calendar = group.getCalendar();

    List<DateRecipe> recipes = calendar.getDateRecipes();

    Recipe recipe = recipeRepository.findById(recipeID).orElse(null);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
    }

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(date);
    dateRecipe.setRecipeID(recipeID);
    dateRecipe.setCalendar(calendar);
    dateRecipe.setStatus(status);

    if (dateRecipeRepository.findByDateAndRecipeIDAndCalendarAndStatus(date, recipeID, calendar, status).size() > 0) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Recipe already exists in Calendar.");
    }

    dateRecipe = dateRecipeRepository.save(dateRecipe);
    recipes.add(dateRecipe);

    calendar = calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Added recipe to Calendar: {}", calendar);

    return calendar;
  }

  // Remove a recipe from the calendar of a user
  public Calendar removeRecipeFromUserCalendar(User user, Long eventId) {

    Calendar calendar = user.getCalendar();

    List<DateRecipe> recipes = calendar.getDateRecipes();

    DateRecipe dateRecipeToRemove = dateRecipeRepository.findById(eventId).orElse(null);
    
    if (dateRecipeToRemove == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found in Calendar.");
    }

    recipes.remove(dateRecipeToRemove);
    calendar.setDateRecipes(recipes);

    dateRecipeRepository.delete(dateRecipeToRemove);
    dateRecipeRepository.flush();

    calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Removed recipe from Calendar: {}", calendar);

    return calendar;
  }

  // Remove a recipe from the calendar of a group
  public Calendar removeRecipeFromGroupCalendar(Group group, Long eventId) {

    Calendar calendar = group.getCalendar();

    List<DateRecipe> recipes = calendar.getDateRecipes();

    DateRecipe dateRecipeToRemove = dateRecipeRepository.findById(eventId).orElse(null);
    
    if (dateRecipeToRemove == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found in Calendar.");
    }

    recipes.remove(dateRecipeToRemove);
    calendar.setDateRecipes(recipes);

    dateRecipeRepository.delete(dateRecipeToRemove);
    dateRecipeRepository.flush();

    calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Removed recipe from Calendar: {}", calendar);

    return calendar;
  }

  // Get the calendar of a user
  public List<CalendarOutput> getUserCalendar(User user) {

    Calendar calendar = user.getCalendar();

    if (calendar == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendar not found.");
    }

    List<DateRecipe> recipes = calendar.getDateRecipes();

    List<CalendarOutput> calendarOutput = new ArrayList<>();

    for (DateRecipe dateRecipe : recipes) {
      CalendarOutput output = new CalendarOutput();
      output.setEventId(dateRecipe.getId());
      output.setDate(dateRecipe.getDate());
      output.setRecipeID(dateRecipe.getRecipeID());
      output.setStatus(dateRecipe.getStatus());

      Recipe recipe = recipeRepository.findById(dateRecipe.getRecipeID()).orElse(null);

      if (recipe == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
      }

      output.setRecipeTitle(recipe.getTitle());
      output.setRecipeImage(recipe.getImage());

      calendarOutput.add(output);
    }

    return calendarOutput;
  }

  // Get the calendar of a group
  public List<CalendarOutput> getGroupCalendar(Group group) {

    Calendar calendar = group.getCalendar();

    if (calendar == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Calendar not found.");
    }

    List<DateRecipe> recipes = calendar.getDateRecipes();

    List<CalendarOutput> calendarOutput = new ArrayList<>();

    for (DateRecipe dateRecipe : recipes) {
      CalendarOutput output = new CalendarOutput();
      output.setEventId(dateRecipe.getId());
      output.setDate(dateRecipe.getDate());
      output.setRecipeID(dateRecipe.getRecipeID());
      output.setStatus(dateRecipe.getStatus());

      Recipe recipe = recipeRepository.findById(dateRecipe.getRecipeID()).orElse(null);

      if (recipe == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
      }

      output.setRecipeTitle(recipe.getTitle());
      output.setRecipeImage(recipe.getImage());

      calendarOutput.add(output);
    }

    return calendarOutput;
  }

}