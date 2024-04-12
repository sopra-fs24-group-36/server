package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.RecipeService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeController {

  private final RecipeService recipeService;

  RecipeController(RecipeService recipeService) {this.recipeService = recipeService; }

  @PostMapping("/users/{userID}/cookbooks")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public RecipeDTO createRecipe(@PathVariable("userID") Long userID, @RequestBody RecipePostDTO recipePostDTO) {

    //needs to change or add a new one because the function takes a User and we only have toe userID
    //UserService.checkIfUserExists(userID);

    Recipe recipeInput = DTOMapper.INSTANCE.convertRecipePostDTOtoEntity(recipePostDTO);

    // Extract the userID from the path and pass it along with the recipePostDTO
    Recipe createdRecipe = recipeService.createRecipe(userID, recipeInput);

    // Returns the createdRecipe and maps it using the Data Transfer Object Mapper to only give necessary information back
    return DTOMapper.INSTANCE.convertEntityToRecipeDTO(createdRecipe);
  }

  //more here
}
