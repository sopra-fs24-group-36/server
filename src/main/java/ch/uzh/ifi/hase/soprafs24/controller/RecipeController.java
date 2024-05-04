package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.RecipeService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class RecipeController {

  private final RecipeService recipeService;
  private final RecipeRepository recipeRepository;
  private final GroupRepository groupRepository;

  private final CommentRepository commentRepository;

  RecipeController(RecipeService recipeService, RecipeRepository recipeRepository, GroupRepository groupRepository, CommentRepository commentRepository) {
      this.recipeService = recipeService;
      this.recipeRepository = recipeRepository;
      this.groupRepository = groupRepository;
      this.commentRepository = commentRepository;
  }

  @PostMapping("/users/{userID}/cookbooks")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public RecipeDTO createUserRecipe(@PathVariable("userID") Long userID, @RequestBody RecipePostDTO recipePostDTO) {
    try {
      Recipe recipeInput = DTOMapper.INSTANCE.convertRecipePostDTOtoEntity(recipePostDTO);

      // Extract the userID from the path and pass it along with the recipePostDTO
      Recipe createdRecipe = recipeService.createUserRecipe(userID, recipeInput);

      // Returns the createdRecipe and maps it using the Data Transfer Object Mapper to only give necessary information back
      return DTOMapper.INSTANCE.convertEntityToRecipeDTO(createdRecipe);
    } catch (Exception e) {
      // If an exception occurs during the conversion process, return HTTP error 409 (Conflict)
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create recipe. Check your input data.", e);
    }
  }

  @PostMapping("/groups/{groupID}/cookbooks")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public RecipeDTO createGroupRecipe(@PathVariable("groupID") Long groupID, @RequestBody RecipePostDTO recipePostDTO) {
    try {
      Recipe recipeInput = DTOMapper.INSTANCE.convertRecipePostDTOtoEntity(recipePostDTO);

      // Extract the groupID from the path and pass it along with the recipePostDTO
      Recipe createdRecipe = recipeService.createGroupRecipe(groupID, recipeInput);

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
  public RecipeDTO getUserRecipe(@PathVariable("recipeID") long recipeID) {
    
    Recipe recipe = recipeService.findRecipeById(recipeID);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
    }

    return DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe);
  }
  
  @GetMapping("/groups/{groupID}/cookbooks/{recipeID}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RecipeDTO getGroupRecipe(@PathVariable("recipeID") long recipeID) {
    
    Recipe recipe = recipeService.findRecipeById(recipeID);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
    }

    return DTOMapper.INSTANCE.convertEntityToRecipeDTO(recipe);
  }

  @PutMapping("/users/{userID}/cookbooks/{recipeID}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void editUserRecipe(@PathVariable("recipeID") long recipeID, @RequestBody RecipePutDTO recipePutDTO) {
    
    Recipe recipe = recipeService.findRecipeById(recipeID);

    if (recipe == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
    }

    Recipe recipeToUpdate = DTOMapper.INSTANCE.convertRecipePutDTOtoEntity(recipePutDTO);

    recipeService.updateRecipe(recipeID, recipeToUpdate);

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

  @GetMapping("/groups/{groupID}/cookbooks")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<RecipeDTO> getGroupRecipes(@PathVariable("groupID") Long groupID) {

    Group group = groupRepository.findById(groupID).orElse(null);
    if (group == null){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
    }

    Cookbook cookbook = group.getCookbook();

    List<Long> recipeIDs = cookbook.getRecipes();

    List<Recipe> recipes = new ArrayList<>();

    for (Long id: recipeIDs){
      Recipe recipe = recipeRepository.findById(id).orElse(null);
      if (recipe == null){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
      recipes.add(recipe);
    }

    List<RecipeDTO> returnRecipes = new ArrayList<>();

    for (Recipe r:recipes){
      RecipeDTO returnrecipe = DTOMapper.INSTANCE.convertEntityToRecipeDTO(r);
      returnRecipes.add(returnrecipe);
    }

    return returnRecipes;
  }
  

  @DeleteMapping("/users/{userID}/cookbooks/{recipeID}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRecipe(@PathVariable("recipeID") long recipeID) {
      try {
          Recipe recipeToDelete = recipeRepository.findById(recipeID);
          if (recipeToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
          }
          recipeService.deleteRecipe(recipeToDelete);
      } catch (Exception e) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found", e);
      }
  }

  @PutMapping("groups/{groupID}/cookbooks/{recipeID}")
  @ResponseStatus(HttpStatus.OK)
  public void removeRecipeFromGroup(@PathVariable("groupID") Long groupID, @PathVariable("recipeID") Long recipeID) {
    try{
      recipeService.removeRecipeFromGroup(groupID, recipeID);
    } catch (Exception e){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group or Recipe not found", e);
    }
  }

  @GetMapping("comments/recipes/{recipeID}")
  @ResponseStatus(HttpStatus.OK)
  public List<CommentDTO> getRecipeComments (@PathVariable Long recipeID) {

      //check that recipe exists
      Recipe recipe = recipeRepository.findById(recipeID).orElse(null);
      if (recipe == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
      }

      //get commentList
      List<Long> commentIDs = recipe.getComments();

      //new list to return
      List<Comment> comments = new ArrayList<>();

      //iterate through all comments and add to new list to return
      for (Long id: commentIDs){
          Comment comment = commentRepository.findById(id).orElse(null);
          if (comment == null){throw new ResponseStatusException(HttpStatus.NOT_FOUND);}
          else {comments.add(comment);}
      }

      List<CommentDTO> returnComments = new ArrayList<>();

      for (Comment c:comments){
          CommentDTO returncomment = DTOMapper.INSTANCE.convertEntityToCommentDTO(c);
          returnComments.add(returncomment);
      }

      return returnComments;

  }

}
