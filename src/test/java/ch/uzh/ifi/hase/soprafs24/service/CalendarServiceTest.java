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
import ch.uzh.ifi.hase.soprafs24.rest.dto.DateRecipeDTO;
import jnr.ffi.annotations.In;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mapstruct.Qualifier;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CalendarServiceTest {

  @Mock
  private CalendarRepository calendarRepository;

  @Mock
  private DateRecipeRepository dateRecipeRepository;

  @Mock
  private RecipeRepository recipeRepository;

  @InjectMocks
  private CalendarService calendarService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void createCalendar_validInput_calendarCreated() {
    // given
    Calendar calendar = new Calendar();
    Mockito.when(calendarRepository.save(Mockito.any())).thenReturn(calendar);
    
    // when
    Calendar createdcalendar = calendarService.createCalendar(calendar);

    // then
    assertNotNull(createdcalendar);
    assertEquals(calendar, createdcalendar);
    verify(calendarRepository, times(1)).save(any());
  }

  @Test
  public void addRecipeToUserCalendar_validInput_recipeAdded() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Recipe recipe = new Recipe();
    recipe.setId(1L);

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(1L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
    when(calendarRepository.save(any())).thenReturn(calendar);
    when(dateRecipeRepository.save(any())).thenReturn(dateRecipe);

    Calendar newcalendar = calendarService.addRecipeToUserCalendar(user, 1L, new Date(), CalendarStatus.BREAKFAST);

    // then
    assertNotNull(newcalendar);
    assertEquals(1, newcalendar.getDateRecipes().size());
  }

  @Test
  public void addRecipeToUserCalendar_invalidRecipeId_exceptionThrown() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    when(recipeRepository.findById(any())).thenReturn(Optional.empty());

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToUserCalendar(user, 1L, new Date(), CalendarStatus.BREAKFAST));
  }

  @Test
  public void addRecipeToUserCalendar_recipeAlreadyExists_exceptionThrown() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Recipe recipe = new Recipe();
    recipe.setId(1L);

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(1L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
    when(dateRecipeRepository.findByDateAndRecipeIDAndCalendarAndStatus(any(), any(), any(), any())).thenReturn(List.of(dateRecipe));

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToUserCalendar(user, 1L, new Date(), CalendarStatus.BREAKFAST));
  }

  @Test
  public void addRecipeToGroupCalendar_validInput_recipeAdded() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(1L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
    when(calendarRepository.save(any())).thenReturn(calendar);
    when(dateRecipeRepository.save(any())).thenReturn(dateRecipe);

    Calendar newcalendar = calendarService.addRecipeToGroupCalendar(group, 1L, new Date(), CalendarStatus.BREAKFAST);

    // then
    assertNotNull(newcalendar);
    assertEquals(1, newcalendar.getDateRecipes().size());
  }

  @Test
  public void addRecipeToGroupCalendar_invalidRecipeId_exceptionThrown() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);

    when(recipeRepository.findById(any())).thenReturn(Optional.empty());

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToGroupCalendar(group, 1L, new Date(), CalendarStatus.BREAKFAST));
  }

  @Test
  public void addRecipeToGroupCalendar_recipeAlreadyExists_exceptionThrown() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(1L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    when(recipeRepository.findById(any())).thenReturn(Optional.of(recipe));
    when(dateRecipeRepository.findByDateAndRecipeIDAndCalendarAndStatus(any(), any(), any(), any())).thenReturn(List.of(dateRecipe));

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.addRecipeToGroupCalendar(group, 1L, new Date(), CalendarStatus.BREAKFAST));
  }

  @Test
  public void removeRecipeFromUserCalendar_validInput_recipeDeleted() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Recipe recipe = new Recipe();
    recipe.setId(1L);

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setId(1L);
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(1L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    when(dateRecipeRepository.findById(any())).thenReturn(Optional.of(dateRecipe));
    when(calendarRepository.save(any())).thenReturn(calendar);

    Calendar newcalendar = calendarService.removeRecipeFromUserCalendar(user, 1L);

    // then
    assertNotNull(newcalendar);
    assertEquals(0, newcalendar.getDateRecipes().size());
  }

  @Test
  public void removeRecipeFromUserCalendar_invalidRecipeId_exceptionThrown() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    when(dateRecipeRepository.findById(any())).thenReturn(Optional.empty());

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromUserCalendar(user, 1L));
  }

  @Test
  public void removeRecipeFromGroupCalendar_validInput_recipeDeleted() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);

    DateRecipe dateRecipe = new DateRecipe();
    dateRecipe.setId(1L);
    dateRecipe.setDate(new Date());
    dateRecipe.setRecipeID(1L);
    dateRecipe.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe.setCalendar(calendar);

    when(dateRecipeRepository.findById(any())).thenReturn(Optional.of(dateRecipe));
    when(calendarRepository.save(any())).thenReturn(calendar);

    Calendar newcalendar = calendarService.removeRecipeFromGroupCalendar(group, 1L);

    // then
    assertNotNull(newcalendar);
    assertEquals(0, newcalendar.getDateRecipes().size());
  }

  @Test
  public void removeRecipeFromGroupCalendar_invalidRecipeId_exceptionThrown() {

    // given
    Calendar calendar = new Calendar();
    calendar.setId(1L);
    calendar.setDateRecipes(new ArrayList<>());

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);

    when(dateRecipeRepository.findById(any())).thenReturn(Optional.empty());

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.removeRecipeFromGroupCalendar(group, 1L));
  }

  @Test
  public void getUserCalendar_validInput_calendarReturned() {

    Recipe recipe1 = new Recipe();
    recipe1.setId(1L);
    recipe1.setTitle("test1");
    recipe1.setImage("test1");

    Recipe recipe2 = new Recipe();
    recipe2.setId(2L);
    recipe2.setTitle("test2");
    recipe2.setImage("test2");

    DateRecipe dateRecipe1 = new DateRecipe();
    dateRecipe1.setId(1L);
    dateRecipe1.setRecipeID(1L);
    dateRecipe1.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe1.setDate(new Date());

    DateRecipe dateRecipe2 = new DateRecipe();
    dateRecipe2.setId(2L);
    dateRecipe2.setRecipeID(2L);
    dateRecipe2.setStatus(CalendarStatus.LUNCH);
    dateRecipe2.setDate(new Date());
    
    when(recipeRepository.findById(recipe1.getId())).thenReturn(Optional.of(recipe1));
    when(recipeRepository.findById(recipe2.getId())).thenReturn(Optional.of(recipe2));

    // given
    Calendar calendar = new Calendar();
    List<DateRecipe> dateRecipeList = new ArrayList<>();
    dateRecipeList.add(dateRecipe1);
    dateRecipeList.add(dateRecipe2);
    calendar.setDateRecipes(dateRecipeList);
    calendar.setId(1L);

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    // when
    List<CalendarOutput> newcalendar = calendarService.getUserCalendar(user);

    // then
    assertNotNull(newcalendar);
    assertEquals(2, newcalendar.size());
    assertEquals(dateRecipe1.getId(), newcalendar.get(0).getEventId());
    assertEquals(dateRecipe1.getRecipeID(), newcalendar.get(0).getRecipeID());
    assertEquals(dateRecipe1.getStatus(), newcalendar.get(0).getStatus());
    assertEquals(dateRecipe1.getDate(), newcalendar.get(0).getDate());
    assertEquals(recipe1.getTitle(), newcalendar.get(0).getRecipeTitle());
    assertEquals(recipe1.getImage(), newcalendar.get(0).getRecipeImage());
    assertEquals(dateRecipe2.getId(), newcalendar.get(1).getEventId());
    assertEquals(dateRecipe2.getRecipeID(), newcalendar.get(1).getRecipeID());
    assertEquals(dateRecipe2.getStatus(), newcalendar.get(1).getStatus());
    assertEquals(dateRecipe2.getDate(), newcalendar.get(1).getDate());
    assertEquals(recipe2.getTitle(), newcalendar.get(1).getRecipeTitle());
    assertEquals(recipe2.getImage(), newcalendar.get(1).getRecipeImage());
  }

  @Test
  public void getUserCalendar_invalidInput_throwsException() {

    // given
    User user = new User();
    user.setId(1L);
    user.setCalendar(null);

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.getUserCalendar(user));
  }

  @Test
  public void getUserCalendar_invalidInput_recipeNotFound() {

    Recipe recipe1 = new Recipe();
    recipe1.setId(1L);
    recipe1.setTitle("test1");
    recipe1.setImage("test1");

    DateRecipe dateRecipe1 = new DateRecipe();
    dateRecipe1.setId(1L);
    dateRecipe1.setRecipeID(1L);
    dateRecipe1.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe1.setDate(new Date());

    DateRecipe dateRecipe2 = new DateRecipe();
    dateRecipe2.setId(2L);
    dateRecipe2.setRecipeID(2L);
    dateRecipe2.setStatus(CalendarStatus.LUNCH);
    dateRecipe2.setDate(new Date());
    
    when(recipeRepository.findById(recipe1.getId())).thenReturn(Optional.of(recipe1));
    when(recipeRepository.findById(Mockito.anyLong())).thenReturn(null);

    // given
    Calendar calendar = new Calendar();
    List<DateRecipe> dateRecipeList = new ArrayList<>();
    dateRecipeList.add(dateRecipe1);
    dateRecipeList.add(dateRecipe2);
    calendar.setDateRecipes(dateRecipeList);
    calendar.setId(1L);

    User user = new User();
    user.setId(1L);
    user.setCalendar(calendar);

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.getUserCalendar(user));
  }

  @Test
  public void getGroupCalendar_validInput_calendarReturned() {

    Recipe recipe1 = new Recipe();
    recipe1.setId(1L);
    recipe1.setTitle("test1");
    recipe1.setImage("test1");

    Recipe recipe2 = new Recipe();
    recipe2.setId(2L);
    recipe2.setTitle("test2");
    recipe2.setImage("test2");

    DateRecipe dateRecipe1 = new DateRecipe();
    dateRecipe1.setId(1L);
    dateRecipe1.setRecipeID(1L);
    dateRecipe1.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe1.setDate(new Date());

    DateRecipe dateRecipe2 = new DateRecipe();
    dateRecipe2.setId(2L);
    dateRecipe2.setRecipeID(2L);
    dateRecipe2.setStatus(CalendarStatus.LUNCH);
    dateRecipe2.setDate(new Date());
    
    when(recipeRepository.findById(recipe1.getId())).thenReturn(Optional.of(recipe1));
    when(recipeRepository.findById(recipe2.getId())).thenReturn(Optional.of(recipe2));

    // given
    Calendar calendar = new Calendar();
    List<DateRecipe> dateRecipeList = new ArrayList<>();
    dateRecipeList.add(dateRecipe1);
    dateRecipeList.add(dateRecipe2);
    calendar.setDateRecipes(dateRecipeList);
    calendar.setId(1L);

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);

    // when
    List<CalendarOutput> newcalendar = calendarService.getGroupCalendar(group);

    // then
    assertNotNull(newcalendar);
    assertEquals(2, newcalendar.size());
    assertEquals(dateRecipe1.getId(), newcalendar.get(0).getEventId());
    assertEquals(dateRecipe1.getRecipeID(), newcalendar.get(0).getRecipeID());
    assertEquals(dateRecipe1.getStatus(), newcalendar.get(0).getStatus());
    assertEquals(dateRecipe1.getDate(), newcalendar.get(0).getDate());
    assertEquals(recipe1.getTitle(), newcalendar.get(0).getRecipeTitle());
    assertEquals(recipe1.getImage(), newcalendar.get(0).getRecipeImage());
    assertEquals(dateRecipe2.getId(), newcalendar.get(1).getEventId());
    assertEquals(dateRecipe2.getRecipeID(), newcalendar.get(1).getRecipeID());
    assertEquals(dateRecipe2.getStatus(), newcalendar.get(1).getStatus());
    assertEquals(dateRecipe2.getDate(), newcalendar.get(1).getDate());
    assertEquals(recipe2.getTitle(), newcalendar.get(1).getRecipeTitle());
    assertEquals(recipe2.getImage(), newcalendar.get(1).getRecipeImage());

  }

  @Test
  public void getGroupCalendar_invalidInput_throwsException() {

    // given
    Group group = new Group();
    group.setId(1L);
    group.setCalendar(null);

    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.getGroupCalendar(group));
  }

  @Test
  public void getGroupCalendar_invalidInput_recipeNotFound() {

    Recipe recipe1 = new Recipe();
    recipe1.setId(1L);
    recipe1.setTitle("test1");
    recipe1.setImage("test1");

    DateRecipe dateRecipe1 = new DateRecipe();
    dateRecipe1.setId(1L);
    dateRecipe1.setRecipeID(1L);
    dateRecipe1.setStatus(CalendarStatus.BREAKFAST);
    dateRecipe1.setDate(new Date());

    DateRecipe dateRecipe2 = new DateRecipe();
    dateRecipe2.setId(2L);
    dateRecipe2.setRecipeID(2L);
    dateRecipe2.setStatus(CalendarStatus.LUNCH);
    dateRecipe2.setDate(new Date());
    
    when(recipeRepository.findById(recipe1.getId())).thenReturn(Optional.of(recipe1));
    when(recipeRepository.findById(Mockito.anyLong())).thenReturn(null);

    // given
    Calendar calendar = new Calendar();
    List<DateRecipe> dateRecipeList = new ArrayList<>();
    dateRecipeList.add(dateRecipe1);
    dateRecipeList.add(dateRecipe2);
    calendar.setDateRecipes(dateRecipeList);
    calendar.setId(1L);

    Group group = new Group();
    group.setId(1L);
    group.setCalendar(calendar);
    
    // then
    assertThrows(ResponseStatusException.class, () -> calendarService.getGroupCalendar(group));
  }
}