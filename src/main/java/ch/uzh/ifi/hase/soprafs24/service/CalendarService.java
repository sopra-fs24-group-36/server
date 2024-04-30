package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CalendarRepository;
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


@Service
@Transactional
public class CalendarService {

  private final Logger log = LoggerFactory.getLogger(ShoppingListService.class);

  private final CalendarRepository calendarRepository;

  private final RecipeRepository recipeRepository;

  @Autowired
  public CalendarService(@Qualifier("calendarRepository") CalendarRepository calendarRepository, RecipeRepository recipeRepository) {
    this.calendarRepository = calendarRepository;
    this.recipeRepository = recipeRepository;
  }

  public Calendar createCalendar(Calendar calendar) {

    calendar = calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Created new Calendar: {}", calendar);

    return calendar;
  }

  public Calendar addRecipeToUserCalendar(User user, Long recipeID, Date date) {

    Calendar calendar = user.getCalendar();

    List<DateRecipe> recipes = calendar.getDateRecipes();

    Recipe recipe = recipeRepository.findById(recipeID).orElse(null);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
    }

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(date);
    dateRecipe.setRecipe(recipe);
    recipes.add(dateRecipe);

    calendar = calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Added recipe to Calendar: {}", calendar);

    return calendar;
  }

  public Calendar addRecipeToGroupCalendar(Group group, Long recipeID, Date date) {

    Calendar calendar = group.getCalendar();

    List<DateRecipe> recipes = calendar.getDateRecipes();

    Recipe recipe = recipeRepository.findById(recipeID).orElse(null);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found.");
    }

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(date);
    dateRecipe.setRecipe(recipe);
    recipes.add(dateRecipe);

    calendar = calendarRepository.save(calendar);
    calendarRepository.flush();

    log.debug("Added recipe to Calendar: {}", calendar);

    return calendar;
  }

}