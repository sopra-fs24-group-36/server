package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RecipeService recipeService;

  @MockBean
  private CookbookService cookbookService;


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

      List<Long> cookbooks = new ArrayList<>();
      cookbooks.add(1L);
      cookbooks.add(2L);
      recipe.setCookbooks(cookbooks);

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

      List<Long> cookbookss = new ArrayList<>();
      cookbookss.add(1L);
      cookbookss.add(2L);
      recipePostDTO.setCookbooks(cookbookss);

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
          .andExpect(jsonPath("$.cookbooks[0]", is(1)))
          .andExpect(jsonPath("$.cookbooks[1]", is(2)))
          .andExpect(jsonPath("$.authorID").value(equalTo(recipe.getAuthorID().intValue()))); // Update assertion
    }
    /**
    @Test
    public void createrecipe_inValidInput_recipeCreated() throws Exception {

        recipePostDTO recipePostDTO = new recipePostDTO();
        recipePostDTO.setPassword("TestPassword");
        recipePostDTO.setrecipename("testrecipename");
        recipePostDTO.setEmail("email.email@email.com");
        recipePostDTO.setName("name");

        //mocks the recipe Service, this defines what createrecipe should return if it is called
        given(recipeService.createrecipe(Mockito.any()))
                .willThrow(new IllegalStateException("add recipe failed because recipename already exists"));

        MockHttpServletRequestBuilder postRequest = post("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePostDTO));

        //perform request twice, which should result in the conflict 409
        mockMvc.perform(postRequest);
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }


    //  test the /recipes/login POST Mapping  //
    @Test
    public void logInrecipe_ValidInput() throws Exception {

        recipe recipe = new recipe();
        recipe.setId(1L);
        recipe.setrecipename("testrecipename");
        recipe.setToken("1");
        recipe.setName("name");
        recipe.setPassword("password");
        recipe.setEmail("email.email@email.com");
        Date creationDate = new Date();
        recipe.setCreationDate(creationDate);
        recipe.setStatus(recipeStatus.ONLINE);

        recipePostDTO recipePostDTO = new recipePostDTO();
        recipePostDTO.setPassword("password");
        recipePostDTO.setrecipename("recipename");

         given(recipeService.logIn(Mockito.any())).willReturn(recipe);

         MockHttpServletRequestBuilder postRequest = post("/recipes/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(recipe.getId().intValue())))
                .andExpect(jsonPath("$.recipename", is(recipe.getrecipename())))
                .andExpect(jsonPath("$.email", is(recipe.getEmail())))
                .andExpect(jsonPath("$.creationDate", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$")))
                .andExpect(jsonPath("$.name", is(recipe.getName())))
                .andExpect(jsonPath("$.status", is(recipe.getStatus().toString())));
    }

    @Test
    public void logInrecipe_InValidInput() throws Exception {

        recipePostDTO recipePostDTO = new recipePostDTO();
        recipePostDTO.setPassword("password");
        recipePostDTO.setrecipename("recipename");

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "recipe cannot be logged in"))
                .when(recipeService).logIn(Mockito.any());


        MockHttpServletRequestBuilder postRequest = post("/recipes/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }


    //  test the /recipes/logout/{recipeId} POST Mapping  //
    @Test
    public void logOutrecipe_ValidInput() throws Exception {

        MockHttpServletRequestBuilder postRequest = post("/recipes/logout/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void logOutrecipe_InValidInput() throws Exception {

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "recipe cannot be logged out"))
                .when(recipeService).logOut(Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/recipes/logout/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }


    /*
    @Test
    public void givenrecipes_whenGetrecipes_thenReturnJsonArray() throws Exception {
        // given
        recipe recipe = new recipe();
        recipe.setName("Firstname Lastname");
        recipe.setrecipename("firstname@lastname");
        recipe.setStatus(recipeStatus.OFFLINE);

        List<recipe> allrecipes = Collections.singletonList(recipe);

        // this mocks the recipeService -> we define above what the recipeService should
        // return when getrecipes() is called
        given(recipeService.getrecipes()).willReturn(allrecipes);

        // when
        MockHttpServletRequestBuilder getRequest = get("/recipes").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(recipe.getName())))
                .andExpect(jsonPath("$[0].recipename", is(recipe.getrecipename())))
                .andExpect(jsonPath("$[0].status", is(recipe.getStatus().toString())));
    }
    */

  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}