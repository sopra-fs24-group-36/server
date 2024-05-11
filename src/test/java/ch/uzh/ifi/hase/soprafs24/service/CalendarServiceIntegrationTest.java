package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarOutput;
import ch.uzh.ifi.hase.soprafs24.entity.DateRecipe;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
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

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
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
    dateRecipeRepository.deleteAll();
    calendarRepository.deleteAll();
    recipeRepository.deleteAll();
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

  @Test
  public void addRecipeToUserCalendar_validInputs_success() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    // when
    Calendar updatedCalendar = calendarService.addRecipeToUserCalendar(testUser, testRecipe.getId(), testDate, testStatus);

    // then
    List<DateRecipe> dateRecipes = updatedCalendar.getDateRecipes();
    assertEquals(1, dateRecipes.size());
    assertEquals(testRecipe.getId(), dateRecipes.get(0).getRecipeID());
    assertEquals(testDate, dateRecipes.get(0).getDate());
    assertEquals(testStatus, dateRecipes.get(0).getStatus());
  }

  @Test
  public void addRecipeToUserCalendar_recipeNotFound_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToUserCalendar(testUser, 2L, testDate, testStatus));
  }

  @Test
  public void addRecipeToUserCalendar_recipeAlreadyExists_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipeRepository.save(dateRecipe);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToUserCalendar(testUser, 1L, testDate, testStatus));
  }

  @Test
  public void removeRecipeFromUserCalendar_validInputs_success() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipe = dateRecipeRepository.save(dateRecipe);

    // when
    Calendar updatedCalendar = calendarService.removeRecipeFromUserCalendar(testUser, dateRecipe.getId());

    // then
    List<DateRecipe> dateRecipes = updatedCalendar.getDateRecipes();
    assertEquals(0, dateRecipes.size());
  }

  @Test
  public void removeRecipeFromUserCalendar_recipeNotFound_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipe = dateRecipeRepository.save(dateRecipe);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromUserCalendar(testUser, 2L));
  }

  @Test
  public void removeRecipeFromUserCalendar_recipeNotInCalendar_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipe = dateRecipeRepository.save(dateRecipe);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromUserCalendar(testUser, 2L));
  }

  @Test
  public void removeRecipeFromUserCalendar_calendarHasNoDateRecipes_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromUserCalendar(testUser, 1L));
  }

  @Test
  public void addRecipeToGroupCalendar_validInputs_success() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    // when
    Calendar updatedCalendar = calendarService.addRecipeToGroupCalendar(testGroup, testRecipe.getId(), testDate, testStatus);

    // then
    List<DateRecipe> dateRecipes = updatedCalendar.getDateRecipes();
    assertEquals(1, dateRecipes.size());
    assertEquals(testRecipe.getId(), dateRecipes.get(0).getRecipeID());
    assertEquals(testDate, dateRecipes.get(0).getDate());
    assertEquals(testStatus, dateRecipes.get(0).getStatus());
  }

  @Test
  public void addRecipeToGroupCalendar_recipeNotFound_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToGroupCalendar(testGroup, 2L, testDate, testStatus));
  }

  @Test
  public void addRecipeToGroupCalendar_recipeAlreadyExists_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipeRepository.save(dateRecipe);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToGroupCalendar(testGroup, 1L, testDate, testStatus));
  }

  @Test
  public void removeRecipeFromGroupCalendar_validInputs_success() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipe = dateRecipeRepository.save(dateRecipe);

    // when
    Calendar updatedCalendar = calendarService.removeRecipeFromGroupCalendar(testGroup, dateRecipe.getId());

    // then
    List<DateRecipe> dateRecipes = updatedCalendar.getDateRecipes();
    assertEquals(0, dateRecipes.size());
  }

  @Test
  public void removeRecipeFromGroupCalendar_recipeNotFound_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipe = dateRecipeRepository.save(dateRecipe);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromGroupCalendar(testGroup, 2L));
  }

  @Test
  public void removeRecipeFromGroupCalendar_recipeNotInCalendar_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    Recipe testRecipe = new Recipe();
    testRecipe.setId(1L);
    testRecipe = recipeRepository.save(testRecipe);

    Date testDate = new Date();
    CalendarStatus testStatus = CalendarStatus.BREAKFAST;

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setRecipeID(testRecipe.getId());
    dateRecipe.setDate(testDate);
    dateRecipe.setCalendar(testCalendar);
    dateRecipe.setStatus(testStatus);
    dateRecipe = dateRecipeRepository.save(dateRecipe);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromGroupCalendar(testGroup, 2L));
  }

  @Test
  public void removeRecipeFromGroupCalendar_calendarHasNoDateRecipes_throwsException() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromGroupCalendar(testGroup, 1L));
  }

  @Test
  public void getUserCalendar_validInputs_success() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    User testUser = new User();
    testUser.setCalendar(testCalendar);

    // when
    List<CalendarOutput> calendarOutput = calendarService.getUserCalendar(testUser);

    // then
    assertEquals(0, calendarOutput.size());

  }

  @Test
  public void getUserCalendar_calendarNotFound_throwsException() {

    // given
    User testUser = new User();

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.getUserCalendar(testUser));
  }

  @Test
  public void getGroupCalendar_validInputs_success() {

    // given
    Calendar testCalendar = new Calendar();
    testCalendar.setDateRecipes(new ArrayList<>());
    testCalendar = calendarRepository.save(testCalendar);

    Group testGroup = new Group();
    testGroup.setCalendar(testCalendar);

    // when
    List<CalendarOutput> calendarOutput = calendarService.getGroupCalendar(testGroup);

    // then
    assertEquals(0, calendarOutput.size());
  }

  @Test
  public void getGroupCalendar_calendarNotFound_throwsException() {

    // given
    Group testGroup = new Group();

    // when
    assertThrows(ResponseStatusException.class, () -> calendarService.getGroupCalendar(testGroup));
  }

}
