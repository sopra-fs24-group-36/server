package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;

import ch.uzh.ifi.hase.soprafs24.rest.dto.VotingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeServiceTest {

  @Mock
  private RecipeRepository recipeRepository;

  @Mock
  private CookbookRepository cookbookRepository;

  @Mock
  private GroupRepository groupRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private CommentService commentService;

  @Mock
  private CalendarRepository calendarRepository;

  @Mock
  private DateRecipeRepository dateRecipeRepository;

  @InjectMocks
  private RecipeService recipeService;

  private User testUser;
  private Group testGroup;
  private Recipe testRecipe;

  @BeforeEach
  public void setup() {
      MockitoAnnotations.openMocks(this);
      testUser = new User();
      testUser.setId(1L);
      testUser.setName("name");
      testUser.setEmail("email.email@email.com");
      testUser.setPassword("password");
      testUser.setUsername("username");
  
      // Create a test recipe
      testRecipe = new Recipe();
      testRecipe.setId(1L);
      testRecipe.setTitle("test");
      testRecipe.setShortDescription("test");
      testRecipe.setLink("test");
      testRecipe.setCookingTime("test");
      List<String> amounts = new ArrayList<>();
      amounts.add("test");
      testRecipe.setAmounts(amounts);
      List<String> ingredients = new ArrayList<>();
      ingredients.add("test");
      testRecipe.setIngredients(ingredients);
      List<String> instructions = new ArrayList<>();
      instructions.add("test");
      testRecipe.setInstructions(instructions);
      testRecipe.setImage("test");
      List<RecipeTags> testtags = new ArrayList<>();
      testtags.add(RecipeTags.VEGAN);
      testRecipe.setTags(testtags);
      List<Long> testgroups = new ArrayList<>();
      testRecipe.setGroups(testgroups);
      testRecipe.setAuthorID(1L);
      recipeRepository.save(testRecipe);


      // Create a test group
      testGroup = new Group();
      testGroup.setId(1L);
      testGroup.setName("Test Group");

      // Mock userRepository to return the test user when findById is called with any Long argument
      Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));

      // Mock groupRepository to return the test group when findById is called with any Long argument
      Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testGroup));

      Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(testRecipe);
  }

  @Test
  public void createUserRecipe_ValidInput_CreatesRecipe() {
    Long userID = 1L;
    Cookbook cookbook = new Cookbook();
    List<Long> recipes = new ArrayList<>();
    cookbook.setRecipes(recipes);
    User user = new User();
    user.setCookbook(cookbook);

  
    // Arrange
    Mockito.when(userRepository.findById(userID)).thenReturn(java.util.Optional.of(user));
    Mockito.when(recipeRepository.save(Mockito.any())).thenAnswer(invocation -> {
        Recipe savedRecipe = invocation.getArgument(0);
        savedRecipe.setId(1L); // Set ID for the saved recipe
        return savedRecipe;
    });

    // Act
    Recipe createdRecipe = recipeService.createUserRecipe(1L, testRecipe);

    // Assert
    assertNotNull(createdRecipe.getId()); // Ensure ID is assigned to the created recipe
    assertEquals(testRecipe.getTitle(), createdRecipe.getTitle());
    assertEquals(testRecipe.getId(), createdRecipe.getId());
    assertEquals(testRecipe.getShortDescription(), createdRecipe.getShortDescription());
    assertEquals(testRecipe.getLink(), createdRecipe.getLink());
    assertEquals(testRecipe.getCookingTime(), createdRecipe.getCookingTime());
    assertEquals(testRecipe.getAmounts(), createdRecipe.getAmounts());
    assertEquals(testRecipe.getIngredients(), createdRecipe.getIngredients());
    assertEquals(testRecipe.getInstructions(), createdRecipe.getInstructions());
    assertEquals(testRecipe.getTags(), createdRecipe.getTags());
    assertEquals(testRecipe.getGroups(), createdRecipe.getGroups());
    assertEquals(testRecipe.getAuthorID(), createdRecipe.getAuthorID());
  }

  @Test
  public void createUserRecipe_ValidInputMoreGroups_CreatesRecipe() {

    Long userID = 1L;
    Cookbook cookbook = new Cookbook();
    List<Long> recipes = new ArrayList<>();
    cookbook.setRecipes(recipes);
    User user = new User();
    user.setId(1L);
    user.setCookbook(cookbook);

    Group group = new Group();
    group.setId(2L);
    Cookbook groupcookbook = new Cookbook();
    groupcookbook.setRecipes(new ArrayList<>());
    groupcookbook.setStatus(CookbookStatus.GROUP);
    group.setCookbook(groupcookbook);

    List<Long> groups = new ArrayList<>();
    groups.add(2L);
    testRecipe.setGroups(groups);

  
    // Arrange
    Mockito.when(userRepository.findById(userID)).thenReturn(Optional.of(user));
    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(group));

    // Act
    Recipe createdRecipe = recipeService.createUserRecipe(1L, testRecipe);

    // Assert
    assertNotNull(createdRecipe.getId()); // Ensure ID is assigned to the created recipe
    assertEquals(testRecipe.getTitle(), createdRecipe.getTitle());
    assertEquals(testRecipe.getId(), createdRecipe.getId());
    assertEquals(testRecipe.getShortDescription(), createdRecipe.getShortDescription());
    assertEquals(testRecipe.getLink(), createdRecipe.getLink());
    assertEquals(testRecipe.getCookingTime(), createdRecipe.getCookingTime());
    assertEquals(testRecipe.getAmounts(), createdRecipe.getAmounts());
    assertEquals(testRecipe.getIngredients(), createdRecipe.getIngredients());
    assertEquals(testRecipe.getInstructions(), createdRecipe.getInstructions());
    assertEquals(testRecipe.getTags(), createdRecipe.getTags());
    assertEquals(testRecipe.getGroups(), createdRecipe.getGroups());
    assertEquals(testRecipe.getAuthorID(), createdRecipe.getAuthorID());
    assertTrue(group.getCookbook().getRecipes().contains(1L));
  }

  @Test
  public void createUserRecipe_InvalidInput_throwsException() {
    Long userID = 1L;
    Cookbook cookbook = new Cookbook();
    List<Long> recipes = new ArrayList<>();
    cookbook.setRecipes(recipes);
    User user = new User();
    user.setCookbook(cookbook);
    List<Long> groups = new ArrayList<>();
    groups.add(6L);
    testRecipe.setGroups(groups);

    // Arrange
    Mockito.when(userRepository.findById(userID)).thenReturn(java.util.Optional.of(user));
    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
    Mockito.when(recipeRepository.save(Mockito.any())).thenAnswer(invocation -> {
        Recipe savedRecipe = invocation.getArgument(0);
        savedRecipe.setId(1L); // Set ID for the saved recipe
        return savedRecipe;
    });
    // Assert
    assertThrows(ResponseStatusException.class, () -> recipeService.createUserRecipe(1L, testRecipe));
  }

  @Test
  public void createUserRecipe_InvalidUser_throwsException() {

    // Arrange
    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
    Mockito.when(recipeRepository.save(Mockito.any())).thenAnswer(invocation -> {
        Recipe savedRecipe = invocation.getArgument(0);
        savedRecipe.setId(1L); // Set ID for the saved recipe
        return savedRecipe;
    });
    // Assert
    assertThrows(ResponseStatusException.class, () -> recipeService.createUserRecipe(1L, testRecipe));
  }

  @Test
  public void createGroupRecipeMoreGroups_validInputs_success() {
    List<Long> testgroups = new ArrayList<>();
    testgroups.add(5L);

    testRecipe.setGroups(testgroups);
    
    Cookbook cookbook = new Cookbook();
    cookbook.setRecipes(new ArrayList<>());
    cookbook.setStatus(CookbookStatus.GROUP);
    Group group = new Group();
    group.setName("test");
    group.setMembers(new ArrayList<>());
    group.setMembersNames(new ArrayList<>());
    group.setCookbook(cookbook);
    group.setImage("test");
    cookbookRepository.save(cookbook);
    groupRepository.save(group);

    Group group2 = new Group();
    Cookbook cookbook2 = new Cookbook();
    cookbook2.setRecipes(new ArrayList<>());
    cookbook2.setStatus(CookbookStatus.GROUP);
    group2.setCookbook(cookbook2);
    group2.setName("test2");
    cookbookRepository.save(cookbook2);
    groupRepository.save(group2);
    group2.setId(5L);

    Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(group2));
    // when
    Recipe createdRecipe = recipeService.createGroupRecipe(group.getId(), testRecipe);

    // then
    assertEquals(testRecipe.getId(), createdRecipe.getId());
    assertTrue(group2.getCookbook().getRecipes().contains(testRecipe.getId()));
  }

  @Test
  public void createGroupRecipeMoreGroups_invalidInputs_throwsException() throws Exception{
    List<Long> testgroups = new ArrayList<>();
    testgroups.add(5L);

    testRecipe.setGroups(testgroups);
    
    Cookbook cookbook = new Cookbook();
    cookbook.setRecipes(new ArrayList<>());
    cookbook.setStatus(CookbookStatus.GROUP);
    Group group = new Group();
    group.setName("test");
    group.setMembers(new ArrayList<>());
    group.setMembersNames(new ArrayList<>());
    group.setCookbook(cookbook);
    group.setImage("test");
    cookbookRepository.save(cookbook);
    groupRepository.save(group);

    Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
    // then
    assertThrows(ResponseStatusException.class, () -> recipeService.createGroupRecipe(group.getId(), testRecipe));
  }

  @Test
  public void findAllRecipesWithUserID_UserWithRecipes_SuccessfullyRetrievesRecipes() {
      // Mock data
      Long userId = 1L;
      List<Recipe> expectedRecipes = new ArrayList<>();
      expectedRecipes.add(new Recipe());
      expectedRecipes.add(new Recipe());

      // Mock the repository to return the expected recipes
      when(recipeRepository.findByAuthorID(userId)).thenReturn(expectedRecipes);

      // Call the method under test
      List<Recipe> actualRecipes = recipeService.findAllRecipesWithUserID(userId);

      // Assert that the retrieved recipes match the expected ones
      assertEquals(expectedRecipes.size(), actualRecipes.size());
      for (int i = 0; i < expectedRecipes.size(); i++) {
          assertEquals(expectedRecipes.get(i), actualRecipes.get(i));
      }
  }

  @Test
  public void updateRecipe_ValidInput_SuccessfullyUpdatesRecipe() {
      // Mock data
      long recipeId = 1L;
      Recipe existingRecipe = new Recipe();
      existingRecipe.setId(recipeId);
      existingRecipe.setShortDescription("Old Description");
  
      Recipe updatedRecipe = new Recipe();
      updatedRecipe.setId(recipeId);
      updatedRecipe.setShortDescription("New Description");
  
      // Mock behavior of recipeRepository.findById
      when(recipeRepository.findById(Mockito.anyLong())).thenReturn(existingRecipe);
  
      // Call the method under test
      recipeService.updateRecipe(recipeId, updatedRecipe);
  
      // Assert that the existing recipe has been updated correctly
      assertEquals(updatedRecipe.getShortDescription(), existingRecipe.getShortDescription());
  }

  @Test
  public void updateRecipe_ValidInput_SuccessfullyUpdatesRecipeWithNewGroups() {
      // Mock data
      long recipeId = 1L;

      Recipe existingRecipe = new Recipe();
      existingRecipe.setId(recipeId);
      existingRecipe.setShortDescription("Old Description");
      existingRecipe.setGroups(new ArrayList<>());
  
      Recipe updatedRecipe = new Recipe();
      updatedRecipe.setId(recipeId);
      updatedRecipe.setShortDescription("New Description");
      List<Long> list = new ArrayList<>();
      list.add(2L);
      updatedRecipe.setGroups(list);

      Group group = new Group();
      Cookbook cookbook = new Cookbook();
      cookbook.setRecipes(new ArrayList<>());
      group.setCookbook(cookbook);
  
      // Mock behavior of recipeRepository.findById
      when(recipeRepository.findById(Mockito.anyLong())).thenReturn(existingRecipe);
      when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(group));
  
      // Call the method under test
      recipeService.updateRecipe(recipeId, updatedRecipe);
  
      // Assert that the existing recipe has been updated correctly
      assertEquals(updatedRecipe.getShortDescription(), existingRecipe.getShortDescription());
      // Add more assertions for other fields if needed
  }

  @Test
  public void updateRecipe_InalidInput_GroupNotfound() {
      // Mock data
      long recipeId = 1L;

      Recipe existingRecipe = new Recipe();
      existingRecipe.setId(recipeId);
      existingRecipe.setShortDescription("Old Description");
      existingRecipe.setGroups(new ArrayList<>());
  
      Recipe updatedRecipe = new Recipe();
      updatedRecipe.setId(recipeId);
      updatedRecipe.setShortDescription("New Description");
      List<Long> list = new ArrayList<>();
      list.add(2L);
      updatedRecipe.setGroups(list);

      // Mock behavior of recipeRepository.findById
      when(recipeRepository.findById(Mockito.anyLong())).thenReturn(existingRecipe);
      when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
  
      // Assert that the existing recipe has been updated correctly
      assertThrows(ResponseStatusException.class, () -> recipeService.updateRecipe(recipeId, updatedRecipe));
  }

  @Test
  public void updateRecipe_ValidInput_SuccessfullyUpdatesRecipeWithLessGroups() {
      // Mock data
      long recipeId = 1L;

      Recipe existingRecipe = new Recipe();
      existingRecipe.setId(recipeId);
      existingRecipe.setShortDescription("Old Description");
      List<Long> groupsBefore = new ArrayList<>();
      groupsBefore.add(2L);
      existingRecipe.setGroups(groupsBefore);
  
      Recipe updatedRecipe = new Recipe();
      updatedRecipe.setId(recipeId);
      updatedRecipe.setShortDescription("New Description");
      updatedRecipe.setGroups(new ArrayList<>());

      Group group = new Group();
      group.setId(2L);
      Cookbook cookbook = new Cookbook();
      List<Long> recipes = new ArrayList<>();
      recipes.add(1L);
      cookbook.setRecipes(recipes);
      group.setCookbook(cookbook);
      Calendar calendar = new Calendar();
      calendar.setDateRecipes(new ArrayList<>());
      group.setCalendar(calendar);
  
      // Mock behavior of recipeRepository.findById
      when(recipeRepository.findById(Mockito.anyLong())).thenReturn(existingRecipe);
      when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(group));
  
      // Call the method under test
      recipeService.updateRecipe(recipeId, updatedRecipe);
  
      // Assert that the existing recipe has been updated correctly
      assertEquals(updatedRecipe.getShortDescription(), existingRecipe.getShortDescription());
  }

  @Test
  public void updateRecipe_InvalidGroupID_ThrowsException() {
    // Mock the recipe repository to return a recipe when findById is called
    Recipe existingRecipe = new Recipe();
    existingRecipe.setId(1L);
    when(recipeRepository.findById(Mockito.anyLong())).thenReturn(existingRecipe);

    // Mock the group repository to throw an exception when findById is called
    when(groupRepository.findById(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

    // Create a recipe with an invalid group ID to update
    Recipe updatedRecipe = new Recipe();
    updatedRecipe.setId(1L);
    updatedRecipe.setTitle("Updated Title");
    updatedRecipe.setGroups(Collections.singletonList(100L)); // Invalid group ID

    // Verify that trying to update the recipe with an invalid group ID throws an exception
    assertThrows(ResponseStatusException.class, () -> recipeService.updateRecipe(1L, updatedRecipe));
  }

  @Test
  public void deleteRecipe_RemovesRecipeFromGroups_Success() {
    // Create a recipe with associated groups
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    recipe.setGroups(groupIDs);
    recipe.setAuthorID(1L);

    // Mock the necessary repositories and entities
    Group mockGroup = new Group();
    Cookbook mockCookbook = new Cookbook();
    List<DateRecipe> dateRecipes = new ArrayList<>();
    DateRecipe mockDateRecipe = new DateRecipe();
    mockDateRecipe.setRecipeID(recipe.getId());
    dateRecipes.add(mockDateRecipe);
    Calendar calendar = new Calendar();
    calendar.setDateRecipes(dateRecipes);
    mockGroup.setCalendar(calendar);
    List<Long> recipes = new ArrayList<>();
    recipes.add(recipe.getId());
    mockCookbook.setRecipes(recipes);
    mockGroup.setCookbook(mockCookbook);

    User testuser = new User();
    testuser.setId(1L);
    Calendar testcalendar = new Calendar();
    testcalendar.setDateRecipes(new ArrayList<>());
    testuser.setCalendar(testcalendar);

    when(groupRepository.findById(any())).thenReturn(Optional.of(mockGroup));
    when(userRepository.findById(recipe.getAuthorID())).thenReturn(Optional.of(testuser));

    // Execute the deleteRecipe method
    recipeService.deleteRecipe(recipe);

    // Verify that the recipe has been removed from the groups' cookbooks
    verify(groupRepository, times(groupIDs.size())).findById(any());
    verify(cookbookRepository, times(groupIDs.size())).save(any(Cookbook.class));

   // If deleteAll should be called once for each group and once for the author
    verify(dateRecipeRepository, times(groupIDs.size() + 1)).deleteAll(any());
    verify(calendarRepository, times(groupIDs.size() + 1)).save(any(Calendar.class)); // Including the author's calendar
    verify(dateRecipeRepository, times(groupIDs.size() + 1)).deleteAll(any()); // Adjust according to actual logic
    verify(recipeRepository).delete(recipe);
    assertTrue(calendar.getDateRecipes().isEmpty(), "DateRecipes should be removed from the group's calendar");
    assertTrue(testcalendar.getDateRecipes().isEmpty(), "DateRecipes should be removed from the author's calendar");
    assertTrue(mockCookbook.getRecipes().isEmpty(), "Recipe ID should be removed from the cookbook");
    assertTrue(mockCookbook.getRecipes().isEmpty(), "Recipe ID should be removed from the cookbook");
  }

  @Test
  public void deleteRecipe_GroupNotFound_throwsException() {
    // Create a recipe with associated groups
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    recipe.setGroups(groupIDs);

    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

    // Verify that the recipe has been removed from the groups' cookbooks
    assertThrows(ResponseStatusException.class, () -> recipeService.deleteRecipe(recipe));
  }

  @Test
  public void deleteRecipe_RemovesRecipeFromGroups_throwsException() {
    // Create a recipe with associated groups
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    recipe.setGroups(groupIDs);

    // Mock the necessary repositories
    Group mockGroup = new Group();
    Cookbook mockCookbook = new Cookbook();
    List<Long> recipes = new ArrayList<>();
    mockCookbook.setRecipes(recipes);
    mockGroup.setCookbook(mockCookbook); //recipe is not part of this cookbook

    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));

    mockGroup.setCookbook(mockCookbook);
    
    assertThrows(ResponseStatusException.class, () -> recipeService.deleteRecipe(recipe));
  }

  @Test
  public void removeRecipeFromGroup_SuccessfullyRemovesRecipe() {

    //create new recipe
    Long recipeId = 2L;
    Recipe mockRecipe = new Recipe();
    mockRecipe.setId(recipeId);

    // Create a mock group and cookbook
    Group mockGroup = new Group();
    mockGroup.setId(1L);
    mockGroup.setName("Name");
    Cookbook mockCookbook = new Cookbook();

    //add groupId to recipe
    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    mockRecipe.setGroups(groupIDs);

    //add recipeId to cookbook
    List<Long> recipes = new ArrayList<>();
    recipes.add(2L);
    mockCookbook.setRecipes(recipes);

    mockGroup.setCookbook(mockCookbook); //recipe is part of this cookbook

    // Mock the group repository to return the mock group
    when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));
    //mock the recipe repository to return the mockRecipe
    when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

    // Call the method under test
    recipeService.removeRecipeFromGroup(1L, recipeId);

    // Verify that the recipe has been removed from the group's cookbook
    Mockito.verify(cookbookRepository).save(mockCookbook);
  }


    @Test
    public void removeRecipeFromGroup_InValidGroupNotFound_ThrowsException() {

        //create new recipe
        Long recipeId = 2L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);

        // Mock the group repository to return an exception
        when(groupRepository.findById(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        //mock the recipe repository to return the mockRecipe
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(3L, recipeId));
    }

    @Test
    public void removeRecipeFromGroup_InValidRecipeNull_ThrowsException() {

        //create new recipe
        Long recipeId = 2L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);

        // Create a mock group and cookbook
        Group mockGroup = new Group();
        mockGroup.setId(1L);
        mockGroup.setName("Name");
        Cookbook mockCookbook = new Cookbook();
        mockGroup.setCookbook(mockCookbook);

        // Mock the group repository to return the mock group
        when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));
        //mock the recipe repository to return the mockRecipe
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(1L, recipeId));
    }


    @Test
    public void removeRecipeFromGroup_InValidRecipeNotInGroup_ThrowsException() {

        //create new recipe
        Long recipeId = 2L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);

        // Create a mock group and cookbook
        Group mockGroup = new Group();
        mockGroup.setId(1L);
        mockGroup.setName("Name");
        Cookbook mockCookbook = new Cookbook();

        //add different recipeId to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(8L);
        mockCookbook.setRecipes(recipes);

        mockGroup.setCookbook(mockCookbook); //recipe is part of this cookbook

        // Mock the group repository to return the mock group
        when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));
        //mock the recipe repository to return the mockRecipe
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(1L, recipeId));
    }

    @Test
    public void removeRecipeFromGroup_InValidRecipeNotFound_ThrowsException() {

        Long recipeId = 2L;

        // Create a mock group and cookbook
        Group mockGroup = new Group();
        mockGroup.setId(1L);
        mockGroup.setName("Name");
        Cookbook mockCookbook = new Cookbook();

        //add recipeId to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(2L);
        mockCookbook.setRecipes(recipes);

        mockGroup.setCookbook(mockCookbook); //recipe is part of this cookbook

        // Mock the group repository to return the mock group
        when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));
        //mock the recipe repository to return the mockRecipe
        when(recipeRepository.findById(recipeId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(1L, recipeId));

    }

    @Test
    public void removeRecipeFromGroup_InValidGroupNull_ThrowsException() {

        //create new recipe
        Long recipeId = 2L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);

        // Create a mock group and cookbook
        Group mockGroup = new Group();
        mockGroup.setId(1L);
        mockGroup.setName("Name");
        Cookbook mockCookbook = new Cookbook();

        //add recipeId to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(2L);
        mockCookbook.setRecipes(recipes);

        mockGroup.setCookbook(mockCookbook); //recipe is part of this cookbook

        // Mock the group repository to return the mock group
        when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));
        //mock the recipe repository to return the mockRecipe
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(1L, recipeId));
    }


    @Test
    public void removeRecipeFromGroup_InValidGroupNotInRecipe_ThrowsException() {

        //create new recipe
        Long recipeId = 2L;
        Recipe mockRecipe = new Recipe();
        mockRecipe.setId(recipeId);

        // Create a mock group and cookbook
        Group mockGroup = new Group();
        mockGroup.setId(1L);
        mockGroup.setName("Name");
        Cookbook mockCookbook = new Cookbook();

        //add different groupId to recipe
        List<Long> groupIDs = new ArrayList<>();
        groupIDs.add(8L);
        mockRecipe.setGroups(groupIDs);

        //add recipeId to cookbook
        List<Long> recipes = new ArrayList<>();
        recipes.add(2L);
        mockCookbook.setRecipes(recipes);

        mockGroup.setCookbook(mockCookbook); //recipe is part of this cookbook

        // Mock the group repository to return the mock group
        when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));
        //mock the recipe repository to return the mockRecipe
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(mockRecipe));

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.removeRecipeFromGroup(1L, recipeId));

    }


    //      voteOnRecipe    //
    @Test
    public void voteOnRecipe_SuccessfullyVotesFirstTime() {

        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testRecipe));

        //create votingDTO
        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setVote(3.0);

        recipeService.voteOnRecipe(testRecipe.getId(), votingDTO);

        Mockito.verify(recipeRepository, Mockito.times(2)).save(Mockito.any());
    }

    @Test
    public void voteOnRecipe_SuccessfullyVotesSecondTime() {

        testRecipe.setCount(1);
        testRecipe.setSum(3L);
        testRecipe.setVote(3.0);

      Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testRecipe));

        //create votingDTO
        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setVote(3.0);

        recipeService.voteOnRecipe(testRecipe.getId(), votingDTO);

        Mockito.verify(recipeRepository, Mockito.times(2)).save(Mockito.any());
    }

    @Test
    public void voteOnRecipe_throwsException_RecipeNotFound() {

        testRecipe.setCount(1);
        testRecipe.setSum(3L);
        testRecipe.setVote(3.0);

        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        //create votingDTO
        VotingDTO votingDTO = new VotingDTO();
        votingDTO.setVote(3.0);

        //call method
        assertThrows(ResponseStatusException.class, () -> recipeService.voteOnRecipe(testRecipe.getId(), votingDTO));
    }


    //      deleteComment   //
    @Test
    public void deleteComment_SuccessfullyDeletesComment() {

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setUsername("username");
        testComment.setText("text");
        testComment.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment.getId());
        comments.add(testComment2.getId());

        testRecipe.setComments(comments);
        recipeRepository.save(testRecipe);

        recipeService.deleteComment(testRecipe, testComment);

        Mockito.verify(recipeRepository, Mockito.times(3)).save(Mockito.any());
    }

    @Test
    public void deleteComment_throwsException_CommentNotInRecipe() {

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setUsername("username");
        testComment.setText("text");
        testComment.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment.getId());

        testRecipe.setComments(comments);
        recipeRepository.save(testRecipe);

        assertThrows(ResponseStatusException.class, () -> recipeService.deleteComment(testRecipe, testComment2));
    }


    //      addComment      //
    @Test
    public void addComment_SuccessfullyAddsComment() {

        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testRecipe));

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setUsername("username");
        testComment.setText("text");
        testComment.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment.getId());

        testRecipe.setComments(comments);
        recipeRepository.save(testRecipe);

        recipeService.addComment(testRecipe.getId(), testComment2);

        Mockito.verify(recipeRepository, Mockito.times(3)).save(Mockito.any());
    }

    @Test
    public void addComment_RecipeNotFound() {

        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setUsername("username");
        testComment.setText("text");
        testComment.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment.getId());

        testRecipe.setComments(comments);
        recipeRepository.save(testRecipe);

        assertThrows(ResponseStatusException.class, () -> recipeService.addComment(testRecipe.getId(), testComment2));
    }

    @Test
    public void addComment_CommentAlreadyInRecipe() {

        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testRecipe));

        Comment testComment = new Comment();
        testComment.setId(1L);
        testComment.setUsername("username");
        testComment.setText("text");
        testComment.setUserID(3L);

        Comment testComment2 = new Comment();
        testComment2.setId(7L);
        testComment2.setUsername("username");
        testComment2.setText("text");
        testComment2.setUserID(3L);

        List<Long> comments = new ArrayList<>();
        comments.add(testComment.getId());
        comments.add(testComment2.getId());

        testRecipe.setComments(comments);
        recipeRepository.save(testRecipe);

        assertThrows(ResponseStatusException.class, () -> recipeService.addComment(testRecipe.getId(), testComment2));
    }


}
