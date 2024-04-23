package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RecipeService recipeService;

  @MockBean
  private CookbookService cookbookService;

  @MockBean
  private RecipeRepository recipeRepository;

  @MockBean
  private GroupRepository groupRepository;

  //  test the /recipes POST Mapping  //
  @Test
  public void createrecipe_validInput_recipeCreated() throws Exception {
    //all fields required by recipe
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    recipe.setTitle("testrecipename");
    recipe.setShortDescription("testdescription");
    recipe.setLink("testlink");
    recipe.setCookingTime("testtime");

    List<String> amounts = new ArrayList<>();
    amounts.add("testamount 1");
    amounts.add("testamount 2");
    recipe.setAmounts(amounts);

    List<String> ingredients = new ArrayList<>();
    ingredients.add("testingredient 1");
    ingredients.add("testingredient 2");
    recipe.setIngredients(ingredients);

    List<String> instructions = new ArrayList<>();
    instructions.add("testinstruction 1");
    instructions.add("testinstruction 2");
    recipe.setInstructions(instructions);

    recipe.setImage("testimage");

    List<RecipeTags> tags = new ArrayList<>();
    tags.add(RecipeTags.VEGAN);
    tags.add(RecipeTags.VEGETARIAN);
    recipe.setTags(tags);

    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    groupIDs.add(2L);
    recipe.setGroups(groupIDs);

    recipe.setAuthorID(1L);


    RecipePostDTO recipePostDTO = new RecipePostDTO();
    recipePostDTO.setTitle("testrecipename");
    recipePostDTO.setShortDescription("testdescription");
    recipePostDTO.setLink("testlink");
    recipePostDTO.setCookingTime("testtime");

    List<String> amountss = new ArrayList<>();
    amountss.add("testamount 1");
    amountss.add("testamount 2");
    recipePostDTO.setAmounts(amountss);

    List<String> ingredientss = new ArrayList<>();
    ingredientss.add("testingredient 1");
    ingredientss.add("testingredient 2");
    recipePostDTO.setIngredients(ingredientss);

    List<String> instructionss = new ArrayList<>();
    instructionss.add("testinstruction 1");
    instructionss.add("testinstruction 2");
    recipePostDTO.setInstructions(instructionss);

    recipePostDTO.setImage("testimage");

    List<RecipeTags> tagss = new ArrayList<>();
    tagss.add(RecipeTags.VEGAN);
    tagss.add(RecipeTags.VEGETARIAN);
    recipePostDTO.setTags(tagss);

    List<Long> groupIDss = new ArrayList<>();
    groupIDss.add(1L);
    groupIDss.add(2L);
    recipePostDTO.setGroups(groupIDss);

    Long authorID = 1L;

    given(recipeService.createUserRecipe(Mockito.eq(authorID), Mockito.any())).willReturn(recipe);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/1/cookbooks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(recipePostDTO));

    // then
    mockMvc.perform(postRequest)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", is(recipe.getId().intValue())))
      .andExpect(jsonPath("$.title", is(recipe.getTitle())))
      .andExpect(jsonPath("$.shortDescription", is(recipe.getShortDescription())))
      .andExpect(jsonPath("$.link", is(recipe.getLink())))
      .andExpect(jsonPath("$.cookingTime", is(recipe.getCookingTime())))
      .andExpect(jsonPath("$.amounts", contains(recipe.getAmounts().toArray())))
      .andExpect(jsonPath("$.ingredients", contains(recipe.getIngredients().toArray())))
      .andExpect(jsonPath("$.instructions", contains(recipe.getInstructions().toArray())))
      .andExpect(jsonPath("$.image", is(recipe.getImage())))
      .andExpect(jsonPath("$.tags[0]", is("VEGAN")))
      .andExpect(jsonPath("$.tags[1]", is("VEGETARIAN")))
      .andExpect(jsonPath("$.groups[0]", is(1)))
      .andExpect(jsonPath("$.groups[1]", is(2)))
      .andExpect(jsonPath("$.authorID").value(equalTo(recipe.getAuthorID().intValue()))); // Update assertion
  }
  
  @Test
  public void createrecipe_invalidInput_throwsException() throws Exception {

    RecipeDTO recipePostDTO = new RecipeDTO();

    given(recipeService.createUserRecipe(Mockito.anyLong(), Mockito.any()))
    .willAnswer(invocation -> {
        throw new Exception();
    });

    MockHttpServletRequestBuilder postRequest = post("/users/1/cookbooks")
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(recipePostDTO));

    // then
    mockMvc.perform(postRequest)
      .andExpect(status().isConflict());
    }

  //  test the /recipes POST Mapping  //
  @Test
  public void creategrouprecipe_validInput_recipeCreated() throws Exception {
    //all fields required by recipe
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    recipe.setTitle("testrecipename");
    recipe.setShortDescription("testdescription");
    recipe.setLink("testlink");
    recipe.setCookingTime("testtime");

    List<String> amounts = new ArrayList<>();
    amounts.add("testamount 1");
    amounts.add("testamount 2");
    recipe.setAmounts(amounts);

    List<String> ingredients = new ArrayList<>();
    ingredients.add("testingredient 1");
    ingredients.add("testingredient 2");
    recipe.setIngredients(ingredients);

    List<String> instructions = new ArrayList<>();
    instructions.add("testinstruction 1");
    instructions.add("testinstruction 2");
    recipe.setInstructions(instructions);

    recipe.setImage("testimage");

    List<RecipeTags> tags = new ArrayList<>();
    tags.add(RecipeTags.VEGAN);
    tags.add(RecipeTags.VEGETARIAN);
    recipe.setTags(tags);

    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    recipe.setGroups(groupIDs);

    RecipePostDTO recipePostDTO = new RecipePostDTO();
    recipePostDTO.setTitle("testrecipename");
    recipePostDTO.setShortDescription("testdescription");
    recipePostDTO.setLink("testlink");
    recipePostDTO.setCookingTime("testtime");

    List<String> amountss = new ArrayList<>();
    amountss.add("testamount 1");
    amountss.add("testamount 2");
    recipePostDTO.setAmounts(amountss);

    List<String> ingredientss = new ArrayList<>();
    ingredientss.add("testingredient 1");
    ingredientss.add("testingredient 2");
    recipePostDTO.setIngredients(ingredientss);

    List<String> instructionss = new ArrayList<>();
    instructionss.add("testinstruction 1");
    instructionss.add("testinstruction 2");
    recipePostDTO.setInstructions(instructionss);

    recipePostDTO.setImage("testimage");

    List<RecipeTags> tagss = new ArrayList<>();
    tagss.add(RecipeTags.VEGAN);
    tagss.add(RecipeTags.VEGETARIAN);
    recipePostDTO.setTags(tagss);

    List<Long> groupIDss = new ArrayList<>();
    groupIDss.add(1L);
    recipePostDTO.setGroups(groupIDss);

    Long groupID = 1L;

    given(recipeService.createGroupRecipe(Mockito.eq(groupID), Mockito.any())).willReturn(recipe);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/groups/1/cookbooks")
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(recipePostDTO));

      
    // then
    mockMvc.perform(postRequest)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id", notNullValue()))
      .andExpect(jsonPath("$.title", is(recipe.getTitle())))
      .andExpect(jsonPath("$.shortDescription", is(recipe.getShortDescription())))
      .andExpect(jsonPath("$.link", is(recipe.getLink())))
      .andExpect(jsonPath("$.cookingTime", is(recipe.getCookingTime())))
      .andExpect(jsonPath("$.amounts", contains(recipe.getAmounts().toArray())))
      .andExpect(jsonPath("$.ingredients", contains(recipe.getIngredients().toArray())))
      .andExpect(jsonPath("$.instructions", contains(recipe.getInstructions().toArray())))
      .andExpect(jsonPath("$.image", is(recipe.getImage())))
      .andExpect(jsonPath("$.tags[0]", is("VEGAN")))
      .andExpect(jsonPath("$.tags[1]", is("VEGETARIAN")))
      .andExpect(jsonPath("$.groups[0]", is(1)));
    }

  @Test
  public void creategrouprecipe_invalidInput_throwsException() throws Exception {
    //all fields required by recipe
    RecipeDTO recipePostDTO = new RecipeDTO();

    given(recipeService.createGroupRecipe(Mockito.anyLong(), Mockito.any()))
    .willAnswer(invocation -> {
        throw new Exception();
    });

    MockHttpServletRequestBuilder postRequest = post("/groups/1/cookbooks")
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(recipePostDTO));

    // then
    mockMvc.perform(postRequest)
      .andExpect(status().isConflict());
  }

  @Test
  public void getUserRecipe_recipeFound_returnRecipeDTO() throws Exception {
    // Given
    //all fields required by recipe
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    recipe.setTitle("testrecipename");
    recipe.setShortDescription("testdescription");
    recipe.setLink("testlink");
    recipe.setCookingTime("testtime");

    List<String> amounts = new ArrayList<>();
    amounts.add("testamount 1");
    amounts.add("testamount 2");
    recipe.setAmounts(amounts);

    List<String> ingredients = new ArrayList<>();
    ingredients.add("testingredient 1");
    ingredients.add("testingredient 2");
    recipe.setIngredients(ingredients);

    List<String> instructions = new ArrayList<>();
    instructions.add("testinstruction 1");
    instructions.add("testinstruction 2");
    recipe.setInstructions(instructions);

    recipe.setImage("testimage");

    List<RecipeTags> tags = new ArrayList<>();
    tags.add(RecipeTags.VEGAN);
    tags.add(RecipeTags.VEGETARIAN);
    recipe.setTags(tags);

    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    groupIDs.add(2L);
    recipe.setGroups(groupIDs);

    recipe.setAuthorID(1L);

    RecipeDTO expectedRecipeDTO = DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe);

    given(recipeService.findRecipeById(1L)).willReturn(recipe);

    // When/Then
    mockMvc.perform(get("/users/1/cookbooks/1"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(expectedRecipeDTO.getId().intValue())))
      .andExpect(jsonPath("$.title", is(expectedRecipeDTO.getTitle())))
      .andExpect(jsonPath("$.shortDescription", is(expectedRecipeDTO.getShortDescription())))
      .andExpect(jsonPath("$.link", is(expectedRecipeDTO.getLink())))
      .andExpect(jsonPath("$.cookingTime", is(expectedRecipeDTO.getCookingTime())))
      .andExpect(jsonPath("$.amounts", contains(expectedRecipeDTO.getAmounts().toArray())))
      .andExpect(jsonPath("$.ingredients", contains(expectedRecipeDTO.getIngredients().toArray())))
      .andExpect(jsonPath("$.instructions", contains(expectedRecipeDTO.getInstructions().toArray())))
      .andExpect(jsonPath("$.image", is(expectedRecipeDTO.getImage())))
      .andExpect(jsonPath("$.tags[0]", is("VEGAN")))
      .andExpect(jsonPath("$.tags[1]", is("VEGETARIAN")))
      .andExpect(jsonPath("$.groups[0]", is(1)))
      .andExpect(jsonPath("$.groups[1]", is(2)))
      .andExpect(jsonPath("$.authorID").value(equalTo(expectedRecipeDTO.getAuthorID().intValue()))); // Update assertion
  }

  @Test
  public void getGroupRecipes_returnsMappedRecipes() throws Exception {
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Group group = new Group();
    Cookbook cookbook = new Cookbook();
    List<Long> list = new ArrayList<>();
    list.add(1L);
    cookbook.setRecipes(list);
    group.setCookbook(cookbook);

    given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));
    given(recipeRepository.findById(Mockito.any())).willReturn(Optional.of(recipe));

    MockHttpServletRequestBuilder getRequest = get("/groups/1/cookbooks")
    .contentType(MediaType.APPLICATION_JSON);

  // then
    mockMvc.perform(getRequest)
      .andExpect(status().isOk());
  }

  @Test
  public void getGroupRecipes_invalid_GroupNotFound() throws Exception {

    given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

    MockHttpServletRequestBuilder getRequest = get("/groups/1/cookbooks")
    .contentType(MediaType.APPLICATION_JSON);

  // then
    mockMvc.perform(getRequest)
      .andExpect(status().isNotFound());
  }

  @Test
  public void getGroupRecipes_invalid_RecipeNotFound() throws Exception {

    Group group = new Group();
    Cookbook cookbook = new Cookbook();
    List<Long> list = new ArrayList<>();
    list.add(1L);
    cookbook.setRecipes(list);
    group.setCookbook(cookbook);

    given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));
    given(recipeRepository.findById(Mockito.any())).willReturn(Optional.empty());

    MockHttpServletRequestBuilder getRequest = get("/groups/1/cookbooks")
    .contentType(MediaType.APPLICATION_JSON);

  // then
    mockMvc.perform(getRequest)
      .andExpect(status().isNotFound());
  }

  @Test
  public void getGroupRecipe_recipeFound_returnRecipeDTO() throws Exception {
    // Given
    //all fields required by recipe
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    recipe.setTitle("testrecipename");
    recipe.setShortDescription("testdescription");
    recipe.setLink("testlink");
    recipe.setCookingTime("testtime");

    List<String> amounts = new ArrayList<>();
    amounts.add("testamount 1");
    amounts.add("testamount 2");
    recipe.setAmounts(amounts);

    List<String> ingredients = new ArrayList<>();
    ingredients.add("testingredient 1");
    ingredients.add("testingredient 2");
    recipe.setIngredients(ingredients);

    List<String> instructions = new ArrayList<>();
    instructions.add("testinstruction 1");
    instructions.add("testinstruction 2");
    recipe.setInstructions(instructions);

    recipe.setImage("testimage");

    List<RecipeTags> tags = new ArrayList<>();
    tags.add(RecipeTags.VEGAN);
    tags.add(RecipeTags.VEGETARIAN);
    recipe.setTags(tags);

    List<Long> groupIDs = new ArrayList<>();
    groupIDs.add(1L);
    groupIDs.add(2L);
    recipe.setGroups(groupIDs);

    RecipeDTO expectedRecipeDTO = DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe);

    given(recipeService.findRecipeById(1L)).willReturn(recipe);

    // When/Then
    mockMvc.perform(get("/groups/1/cookbooks/1"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(expectedRecipeDTO.getId().intValue())))
      .andExpect(jsonPath("$.title", is(expectedRecipeDTO.getTitle())))
      .andExpect(jsonPath("$.shortDescription", is(expectedRecipeDTO.getShortDescription())))
      .andExpect(jsonPath("$.link", is(expectedRecipeDTO.getLink())))
      .andExpect(jsonPath("$.cookingTime", is(expectedRecipeDTO.getCookingTime())))
      .andExpect(jsonPath("$.amounts", contains(expectedRecipeDTO.getAmounts().toArray())))
      .andExpect(jsonPath("$.ingredients", contains(expectedRecipeDTO.getIngredients().toArray())))
      .andExpect(jsonPath("$.instructions", contains(expectedRecipeDTO.getInstructions().toArray())))
      .andExpect(jsonPath("$.image", is(expectedRecipeDTO.getImage())))
      .andExpect(jsonPath("$.tags[0]", is("VEGAN")))
      .andExpect(jsonPath("$.tags[1]", is("VEGETARIAN")))
      .andExpect(jsonPath("$.groups[0]", is(1)))
      .andExpect(jsonPath("$.groups[1]", is(2)));
  }

  @Test
  public void getUserRecipe_recipeNotFound_returnNotFound() throws Exception {
    // Given
    given(recipeService.findRecipeById(1L)).willReturn(null);

    // When/Then
    mockMvc.perform(get("/users/1/cookbooks/1"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void getGroupRecipe_recipeNotFound_returnNotFound() throws Exception {
      // Given
      given(recipeService.findRecipeById(1L)).willReturn(null);

      // When/Then
      mockMvc.perform(get("/groups/1/cookbooks/1"))
              .andExpect(status().isNotFound());
  }

  @Test
  public void editUserRecipe_validInput_recipeUpdated() throws Exception {
      // Create a dummy recipe with ID 1
      Recipe recipe = new Recipe();
      recipe.setId(1L);
      recipe.setTitle("testtitle 1");
      //skipped all other set operations since they are not needed

      // Mock the behavior of recipeService.findRecipeById(1L) to return the dummy recipe
      Mockito.when(recipeService.findRecipeById(1L)).thenReturn(recipe);

      // Construct the request body for the test
      RecipePutDTO recipePutDTO = new RecipePutDTO();
      recipePutDTO.setTitle("testtitle 2");
      // Set properties of the recipePutDTO...

      // Perform the request
      mockMvc.perform(put("/users/1/cookbooks/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(recipePutDTO)))
              .andExpect(status().isNoContent());
  }

  @Test
  public void editUserRecipe_recipeNotFound_404Returned() throws Exception {
    // Mock the behavior of recipeService.findRecipeById(1L) to return null, indicating the recipe does not exist
    Mockito.when(recipeService.findRecipeById(1L)).thenReturn(null);

    // Construct the request body for the test
    RecipePutDTO recipePutDTO = new RecipePutDTO();
    recipePutDTO.setTitle("testtitle 2");
    // Set properties of the recipePutDTO...

    // Perform the request
    mockMvc.perform(put("/users/1/cookbooks/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(asJsonString(recipePutDTO)))
      .andExpect(status().isNotFound());
  }

  @Test
  public void getRecipes_userRecipesFound_returnRecipeDTOList() throws Exception {
    // Given
    // Create a list of dummy recipes
    List<Recipe> dummyRecipes = new ArrayList<>();
    Recipe recipe1 = new Recipe();
    recipe1.setId(1L);
    recipe1.setTitle("testrecipename");
    recipe1.setShortDescription("testdescription");
    recipe1.setLink("testlink");
    recipe1.setCookingTime("testtime");

    List<String> amounts1 = new ArrayList<>();
    amounts1.add("testamount 1");
    amounts1.add("testamount 2");
    recipe1.setAmounts(amounts1);

    List<String> ingredients1 = new ArrayList<>();
    ingredients1.add("testingredient 1");
    ingredients1.add("testingredient 2");
    recipe1.setIngredients(ingredients1);

    List<String> instructions1 = new ArrayList<>();
    instructions1.add("testinstruction 1");
    instructions1.add("testinstruction 2");
    recipe1.setInstructions(instructions1);

    recipe1.setImage("testimage");

    List<RecipeTags> tags1 = new ArrayList<>();
    tags1.add(RecipeTags.VEGAN);
    tags1.add(RecipeTags.VEGETARIAN);
    recipe1.setTags(tags1);

    List<Long> groupIDs1 = new ArrayList<>();
    groupIDs1.add(1L);
    groupIDs1.add(2L);
    recipe1.setGroups(groupIDs1);

    recipe1.setAuthorID(1L);

    Recipe recipe2 = new Recipe();
    recipe2.setId(1L);
    recipe2.setTitle("testrecipename");
    recipe2.setShortDescription("testdescription");
    recipe2.setLink("testlink");
    recipe2.setCookingTime("testtime");

    List<String> amounts2 = new ArrayList<>();
    amounts2.add("testamount 1");
    amounts2.add("testamount 2");
    recipe2.setAmounts(amounts2);

    List<String> ingredients2 = new ArrayList<>();
    ingredients2.add("testingredient 1");
    ingredients2.add("testingredient 2");
    recipe2.setIngredients(ingredients2);

    List<String> instructions2 = new ArrayList<>();
    instructions2.add("testinstruction 1");
    instructions2.add("testinstruction 2");
    recipe2.setInstructions(instructions2);

    recipe2.setImage("testimage");

    List<RecipeTags> tags2 = new ArrayList<>();
    tags2.add(RecipeTags.VEGAN);
    tags2.add(RecipeTags.VEGETARIAN);
    recipe2.setTags(tags2);

    List<Long> groupIDs2 = new ArrayList<>();
    groupIDs2.add(1L);
    groupIDs2.add(2L);
    recipe2.setGroups(groupIDs2);

    recipe2.setAuthorID(1L);

    dummyRecipes.add(recipe1);
    dummyRecipes.add(recipe2);

    // Mock the behavior of recipeService.findAllRecipesWithUserID(1L) to return the list of dummy recipes
    Mockito.when(recipeService.findAllRecipesWithUserID(1L)).thenReturn(dummyRecipes);

    // Convert the list of dummy recipes to a list of expected RecipeDTOs
    List<RecipeDTO> expectedRecipeDTOs = new ArrayList<>();
    expectedRecipeDTOs.add(DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe1));
    expectedRecipeDTOs.add(DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe2));

    // When/Then
    mockMvc.perform(get("/users/1/cookbooks"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$[0].id", is(expectedRecipeDTOs.get(0).getId().intValue())))
      .andExpect(jsonPath("$[0].title", is(expectedRecipeDTOs.get(0).getTitle())))
      .andExpect(jsonPath("$[0].shortDescription", is(expectedRecipeDTOs.get(0).getShortDescription())))
      .andExpect(jsonPath("$[0].link", is(expectedRecipeDTOs.get(0).getLink())))
      .andExpect(jsonPath("$[0].cookingTime", is(expectedRecipeDTOs.get(0).getCookingTime())))
      .andExpect(jsonPath("$[0].amounts", contains(expectedRecipeDTOs.get(0).getAmounts().toArray())))
      .andExpect(jsonPath("$[0].ingredients", contains(expectedRecipeDTOs.get(0).getIngredients().toArray())))
      .andExpect(jsonPath("$[0].instructions", contains(expectedRecipeDTOs.get(0).getInstructions().toArray())))
      .andExpect(jsonPath("$[0].image", is(expectedRecipeDTOs.get(0).getImage())))
      .andExpect(jsonPath("$[0].tags[0]", is("VEGAN")))
      .andExpect(jsonPath("$[0].tags[1]", is("VEGETARIAN")))
      .andExpect(jsonPath("$[0].groups[0]", is(1)))
      .andExpect(jsonPath("$[0].groups[1]", is(2)))
      .andExpect(jsonPath("$[0].authorID").value(equalTo(expectedRecipeDTOs.get(0).getAuthorID().intValue())))
      .andExpect(jsonPath("$[1].id", is(expectedRecipeDTOs.get(1).getId().intValue())))
      .andExpect(jsonPath("$[1].title", is(expectedRecipeDTOs.get(1).getTitle())))
      .andExpect(jsonPath("$[1].shortDescription", is(expectedRecipeDTOs.get(1).getShortDescription())))
      .andExpect(jsonPath("$[1].link", is(expectedRecipeDTOs.get(1).getLink())))
      .andExpect(jsonPath("$[1].cookingTime", is(expectedRecipeDTOs.get(1).getCookingTime())))
      .andExpect(jsonPath("$[1].amounts", contains(expectedRecipeDTOs.get(1).getAmounts().toArray())))
      .andExpect(jsonPath("$[1].ingredients", contains(expectedRecipeDTOs.get(1).getIngredients().toArray())))
      .andExpect(jsonPath("$[1].instructions", contains(expectedRecipeDTOs.get(1).getInstructions().toArray())))
      .andExpect(jsonPath("$[1].image", is(expectedRecipeDTOs.get(1).getImage())))
      .andExpect(jsonPath("$[1].tags[0]", is("VEGAN")))
      .andExpect(jsonPath("$[1].tags[1]", is("VEGETARIAN")))
      .andExpect(jsonPath("$[1].groups[0]", is(1)))
      .andExpect(jsonPath("$[1].groups[1]", is(2)))
      .andExpect(jsonPath("$[1].authorID").value(equalTo(expectedRecipeDTOs.get(1).getAuthorID().intValue())));
  }

  @Test
  public void deleteRecipe_recipeExists_returnNoContent() throws Exception {
      // Mock the behavior of recipeRepository.findById(recipeID) to return a recipe
      Recipe mockRecipe = new Recipe();
      mockRecipe.setId(1L);
      Mockito.when(recipeRepository.findById(Mockito.anyLong())).thenReturn(mockRecipe);

      // Perform the delete request
      mockMvc.perform(delete("/users/1/cookbooks/1"))
              .andExpect(status().isNoContent());
  }

  @Test
  public void deleteRecipe_recipeNotFound_returnNotFound() throws Exception {
      // Mock the behavior of recipeRepository.findById(recipeID) to return null
      Mockito.when(recipeRepository.findById(Mockito.anyLong())).thenReturn(null);

      // Perform the delete request
      mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/cookbooks/1"))
              .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void removeRecipeFromGroup_Success() throws Exception {
      // Mock successful removal of recipe from group
      Group g = new Group();
      g.setName("test");
      groupRepository.save(g);
      Mockito.when(recipeService.removeRecipeFromGroup(Mockito.anyLong(), Mockito.anyLong())).thenReturn(g);
      // Perform PUT request
      mockMvc.perform(put("/groups/1/cookbooks/2"))
              .andExpect(status().isOk());
  }

  @Test
  public void removeRecipeFromGroup_GroupNotFound() throws Exception {
      // Mock service to throw exception for group not found
      Mockito.doThrow(new RuntimeException("Group not found")).when(recipeService).removeRecipeFromGroup(Mockito.anyLong(), Mockito.anyLong());

      // Perform PUT request and expect 404
      mockMvc.perform(put("/groups/1/cookbooks/2"))
              .andExpect(status().isNotFound());
  }

  @Test
  public void removeRecipeFromGroup_RecipeNotFound() throws Exception {
      // Mock service to throw exception for recipe not found
      Mockito.doThrow(new RuntimeException("Recipe not found")).when(recipeService).removeRecipeFromGroup(Mockito.anyLong(), Mockito.anyLong());

      // Perform PUT request and expect 404
      mockMvc.perform(put("/groups/1/cookbooks/2"))
              .andExpect(status().isNotFound());
  }
  
  
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}