package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CalendarRepository;
import ch.uzh.ifi.hase.soprafs24.repository.DateRecipeRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class CalendarServiceIntegrationTest {

  @Qualifier("calendarRepository")
  @Autowired
  private CalendarRepository calendarRepository;

  @Qualifier("dateRecipeRepository")
  @Autowired
  private DateRecipeRepository dateRecipeRepository;

  @Qualifier("recipeRepository")
  @Autowired
  private RecipeRepository recipeRepository;

  @Autowired
  private CalendarService calendarService;

  @BeforeEach
  public void setup() {
    calendarRepository.deleteAll();
    dateRecipeRepository.deleteAll();
  }

  @Test
  public void createCalendar_validInputs_success() {

    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());

    // when
    Calendar createdCalendar = calendarService.createCalendar(testCalendar);

    // then
    assertNotNull(createdCalendar.getId());
    assertEquals(testCalendar.getDateRecipes(), createdCalendar.getDateRecipes());
  }

}
