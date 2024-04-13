package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.RecipeService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RecipeController {

  private final RecipeService recipeService;

  RecipeController(RecipeService recipeService) {this.recipeService = recipeService; }

  @PostMapping("/users/{userID}/cookbooks")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public RecipeDTO createRecipe(@PathVariable("userID") Long userID, @RequestBody RecipePostDTO recipePostDTO) {
    try {
      Recipe recipeInput = DTOMapper.INSTANCE.convertRecipePostDTOtoEntity(recipePostDTO);

      // Extract the userID from the path and pass it along with the recipePostDTO
      Recipe createdRecipe = recipeService.createRecipe(userID, recipeInput);

      // Returns the createdRecipe and maps it using the Data Transfer Object Mapper to only give necessary information back
      return DTOMapper.INSTANCE.convertEntityToRecipeDTO(createdRecipe);
    } catch (Exception e) {
      // If an exception occurs during the conversion process, return HTTP error 409 (Conflict)
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create recipe. Check your input data.", e);
    }
  }

  @GetMapping("/users/{userID}/cookbooks/{recipeID}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RecipeDTO getRecipe(@PathVariable("recipeID") long recipeID) {
    
    Recipe recipe = recipeService.findRecipeById(recipeID);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
    }

    return DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe);
  }
  

  @GetMapping("/users/{userID}/cookbooks")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<RecipeDTO> getRecipes(@PathVariable("userID") Long userID) {
    
    List<Recipe> recipes = recipeService.findAllRecipesWithUserID(userID);

    List<RecipeDTO> mapped_recipes = new ArrayList<>();

    for (Recipe recipe : recipes) {
      mapped_recipes.add(DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe));
    }

    return mapped_recipes;
  }
}
