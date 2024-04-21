package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;

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
      existingRecipe.setTitle("Old Title");
      existingRecipe.setShortDescription("Old Description");
  
      Recipe updatedRecipe = new Recipe();
      updatedRecipe.setId(recipeId);
      updatedRecipe.setTitle("New Title");
      updatedRecipe.setShortDescription("New Description");
  
      // Mock behavior of recipeRepository.findById
      when(recipeRepository.findById(Mockito.anyLong())).thenReturn(existingRecipe);
  
      // Call the method under test
      recipeService.updateRecipe(recipeId, updatedRecipe);
  
      // Assert that the existing recipe has been updated correctly
      assertEquals(updatedRecipe.getTitle(), existingRecipe.getTitle());
      assertEquals(updatedRecipe.getShortDescription(), existingRecipe.getShortDescription());
      // Add more assertions for other fields if needed
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

    // Mock the necessary repositories
    Group mockGroup = new Group();
    Cookbook mockCookbook = new Cookbook();
    List<Long> recipes = new ArrayList<>();
    recipes.add(recipe.getId());
    mockCookbook.setRecipes(recipes);
    mockGroup.setCookbook(mockCookbook);

    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));

    mockGroup.setCookbook(mockCookbook);
    // Call the method under test
    recipeService.deleteRecipe(recipe);

    // Verify that the recipe has been removed from the groups' cookbooks
    Mockito.verify(groupRepository, Mockito.times(groupIDs.size())).findById(Mockito.anyLong());
    Mockito.verify(cookbookRepository, Mockito.times(groupIDs.size())).save(Mockito.any());
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
    // Create a mock group and cookbook
    Group mockGroup = new Group();
    Cookbook mockCookbook = new Cookbook();
    List<Long> recipes = new ArrayList<>();
    recipes.add(2L);
    mockCookbook.setRecipes(recipes);
    mockGroup.setCookbook(mockCookbook); //recipe is part of this cookbook

    // Mock the group repository to return the mock group
    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockGroup));

    // Call the method under test
    recipeService.removeRecipeFromGroup(1L, 2L);

    // Verify that the recipe has been removed from the group's cookbook
    Mockito.verify(cookbookRepository).save(mockCookbook);
  }

  @Test
  public void removeRecipeFromGroup_GroupNotFound_ThrowsException() {
    // Mock the group repository to return empty optional, simulating a group not found scenario
    Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

    // Assert that the method under test throws an exception when the group is not found
    assertThrows(RuntimeException.class, () -> recipeService.removeRecipeFromGroup(1L, 2L));
  }


  


  
  
  
  



}
