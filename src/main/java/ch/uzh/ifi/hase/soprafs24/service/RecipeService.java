package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;

import ch.uzh.ifi.hase.soprafs24.rest.dto.VotingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

// Service to handle all recipe related functionality
@Service
@Transactional
public class RecipeService {

  private final Logger log = LoggerFactory.getLogger(RecipeService.class);

  private final RecipeRepository recipeRepository;

  private final CookbookRepository cookbookRepository;

  private final GroupRepository groupRepository;

  private final UserRepository userRepository;

  private final CommentRepository commentRepository;

  private final CommentService commentService;

  private final CalendarRepository calendarRepository;

  private final DateRecipeRepository dateRecipeRepository;

  @Autowired
  public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository, CommentService commentService, CookbookRepository cookbookRepository, GroupRepository groupRepository, UserRepository userRepository, CommentRepository commentRepository, CalendarRepository calendarRepository, DateRecipeRepository dateRecipeRepository) {
    this.recipeRepository = recipeRepository;
    this.cookbookRepository = cookbookRepository;
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
    this.commentRepository = commentRepository;
    this.commentService = commentService;
    this.calendarRepository = calendarRepository;
    this.dateRecipeRepository = dateRecipeRepository;
  }

  // Create a new recipe for a user
  public Recipe createUserRecipe(Long userID, Recipe newRecipe) {

    newRecipe = recipeRepository.save(newRecipe);

    //something like checkifrecipeexists? probably not because it is in the users responsibility to not save multiple same recipes 
    newRecipe.setAuthorID(userID);

    User author = userRepository.findById(userID).orElse(null);
    if (author == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found!");
    }
    
    List<Long> groupIDs = newRecipe.getGroups();

    User user = author;

    Cookbook cookbook = user.getCookbook();
    List<Long> persrecipes = cookbook.getRecipes();
    persrecipes.add(newRecipe.getId());
    cookbook.setRecipes(persrecipes);
    user.setCookbook(cookbook);
    cookbookRepository.save(cookbook);
    userRepository.save(user);

    for (Long id:groupIDs){
      Group g = groupRepository.findById(id).orElse(null);
      if (g == null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupID not associated with a group");
      }
      Cookbook c = g.getCookbook();
      List<Long> recipes = c.getRecipes();

      recipes.add(newRecipe.getId());
      c.setRecipes(recipes);
      cookbookRepository.save(c);
    }

    cookbookRepository.flush();
    recipeRepository.flush();

    log.debug("Created new Recipe: {}", newRecipe);

    return newRecipe;
  }

  // Create a new recipe for a group
  public Recipe createGroupRecipe(Long groupID, Recipe newRecipe) {

    // Save the new recipe
    newRecipe = recipeRepository.save(newRecipe);

    List<Long> groupIDs = newRecipe.getGroups();

    Group group = groupRepository.findById(groupID).orElse(null);
    if(group == null){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");}


    Cookbook cookbook = group.getCookbook();
    List<Long> grouprecipes = cookbook.getRecipes();
    grouprecipes.add(newRecipe.getId());
    cookbook.setRecipes(grouprecipes);
    group.setCookbook(cookbook);
    cookbookRepository.save(cookbook);
    groupRepository.save(group);
  
    for (Long id:groupIDs){
      Group g = groupRepository.findById(id).orElse(null);
      if (g == null){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GroupID not associated with a group");
      }
      Cookbook c = g.getCookbook();
      List<Long> recipes = c.getRecipes();

      recipes.add(newRecipe.getId());
      c.setRecipes(recipes);
      cookbookRepository.save(c);
    }
    

    // Flush changes to the database
    cookbookRepository.flush();
    recipeRepository.flush();

    log.debug("Created new Recipe: {}", newRecipe);

    return newRecipe;
  }

  // Find a recipe by its ID
  public Recipe findRecipeById(Long recipeID) {
    Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
    return optionalRecipe.orElse(null); // Returns null if recipe is not found
  }

  // Find all recipes of a user
  public List<Recipe> findAllRecipesWithUserID(Long userID) {
    List<Recipe> recipes = recipeRepository.findByAuthorID(userID);

    return recipes;
  }

  // Update a recipe
  public void updateRecipe(long recipeID, Recipe recipeToUpdate){
    String title = recipeToUpdate.getTitle();
    
    String shortDescription = recipeToUpdate.getShortDescription();

    String link = recipeToUpdate.getLink();

    String cookingTime = recipeToUpdate.getCookingTime();

    List<String> amounts = recipeToUpdate.getAmounts();

    List<String> ingredients = recipeToUpdate.getIngredients();

    List<String> instructions = recipeToUpdate.getInstructions();

    String image = recipeToUpdate.getImage();

    List<RecipeTags> tags = recipeToUpdate.getTags();

    List<Long> groupIDs = recipeToUpdate.getGroups();

    try {
      Recipe recipe = recipeRepository.findById(recipeID);
      
      if (title != null){
        recipe.setTitle(title);
      }
      if (shortDescription != null){
        recipe.setShortDescription(shortDescription);
      }
      if (link != null){
        recipe.setLink(link);
      }
      if (cookingTime != null){
        recipe.setCookingTime(cookingTime);
      }
      if (amounts != null){
        recipe.setAmounts(amounts);
      }
      if (ingredients != null){
        recipe.setIngredients(ingredients);
      }
      if (instructions != null){
        recipe.setInstructions(instructions);
      }
      if (image != null){
        recipe.setImage(image);
      }
      if (tags != null){
        recipe.setTags(tags);
      }
      if (groupIDs != null) {
        List<Long> before = recipe.getGroups();
        recipe.setGroups(groupIDs);
        List<Long> after = recipe.getGroups();
    
        // Create copies of the lists to avoid modifying the originals
        List<Long> beforeCopy = new ArrayList<>(before);
        List<Long> afterCopy = new ArrayList<>(after);
    
        // Calculate the elements present in 'after' but not in 'before'
        afterCopy.removeAll(before);

        for (long groupID:afterCopy){
          Group g = groupRepository.findById(groupID).orElse(null);
          if (g==null){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found" + groupID);}
          Cookbook c = g.getCookbook();
          List<Long> recipes = c.getRecipes();
          recipes.add(recipe.getId()); // Add the new recipe to the list of recipes in the cookbook
          c.setRecipes(recipes);
          cookbookRepository.save(c);
          cookbookRepository.flush();
        }
    
        // Calculate the elements present in 'before' but not in 'after'
        beforeCopy.removeAll(after);

        for (long groupID:beforeCopy){
          Group g = groupRepository.findById(groupID).orElse(null);
          if (g==null){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found" + groupID);}
          Cookbook c = g.getCookbook();
          List<Long> recipes = c.getRecipes();
          recipes.remove(recipe.getId()); // Add the new recipe to the list of recipes in the cookbook
          c.setRecipes(recipes);
          cookbookRepository.save(c);
          cookbookRepository.flush();
        }
        

        recipeRepository.save(recipe);
        recipeRepository.flush();
      }
      
  
    } catch(Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong, please check content");
    }

  }

  // Delete a recipe
  public void deleteRecipe(Recipe recipe) {

    //remove recipe from groups it belongs to
    if (recipe.getGroups() != null) {
      for (Long id : recipe.getGroups()) {
        Group g = groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));
        Cookbook c = g.getCookbook();
        List<Long> recipes = c.getRecipes();

        if (recipes.contains(recipe.getId())) {
          recipes.remove(recipe.getId());
          cookbookRepository.save(c);
        } else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe is not part of one of these groups");
        }

        // Handle calendar and dateRecipes for each group
        Calendar calendar = g.getCalendar();
        List<DateRecipe> dateRecipes = new ArrayList<>(calendar.getDateRecipes());
        dateRecipes.removeIf(dr -> dr.getRecipeID().equals(recipe.getId()));
        calendar.setDateRecipes(dateRecipes); // update the calendar's list
        calendarRepository.save(calendar);

        // Delete DateRecipes after removal from list to avoid concurrent modification
        dateRecipeRepository.deleteAll(dateRecipes.stream().filter(dr -> dr.getRecipeID().equals(recipe.getId())).collect(Collectors.toList()));
      }
    }

    // Remove recipe from author's calendar
    Long authorId = recipe.getAuthorID();
    User author = userRepository.findById(authorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));
    Calendar authorCalendar = author.getCalendar();
    List<DateRecipe> authorDateRecipes = new ArrayList<>(authorCalendar.getDateRecipes());
    authorDateRecipes.removeIf(dr -> dr.getRecipeID().equals(recipe.getId()));
    authorCalendar.setDateRecipes(authorDateRecipes); // update the author's calendar
    calendarRepository.save(authorCalendar);

    // Delete Author's DateRecipes
    dateRecipeRepository.deleteAll(authorDateRecipes.stream().filter(dr -> dr.getRecipeID().equals(recipe.getId())).collect(Collectors.toList()));

    //delete comments that belonged to recipe
    if (recipe.getComments() != null) {
      recipe.getComments().forEach(commentID -> {
        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
        commentService.deleteComment(comment);
      });
    }

    // Finally delete the recipe
    recipeRepository.delete(recipe);
  }

  // Remove a recipe from a group
  public Group removeRecipeFromGroup(Long groupID, Long recipeID) {

    //make sure group exists and get group
    Group group = groupRepository.findById(groupID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

    //get cookbook of group and remove recipe, check that recipe found in group
    Cookbook cookbook = group.getCookbook();
    List<Long> recipes = cookbook.getRecipes();
    if (recipes == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found in the group's cookbook with ID: " + recipeID);
    }
    if (!recipes.remove(recipeID)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found in the group's cookbook with ID: " + recipeID);
    }
    cookbook.setRecipes(recipes);
    cookbookRepository.save(cookbook);
    cookbookRepository.flush();
    groupRepository.save(group);
    groupRepository.flush();

    Recipe recipe = recipeRepository.findById(recipeID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
    List<Long> groups = recipe.getGroups();
      if (groups == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found in the recipe's group list with ID: " + groupID);
      }
    if (!groups.remove(groupID)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found in the recipe's group list with ID: " + groupID);
    }
    recipe.setGroups(groups);
    recipeRepository.save(recipe);
    recipeRepository.flush();

    return group;
  }

  // Add a comment to a recipe
  public void addComment (Long recipeID, Comment comment) {

      //check that recipe exists, otherwise exception and delete comment
      Recipe recipe = recipeRepository.findById(recipeID)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found, Comment could not be created"));

      //save commentID to recipe if not already exists
      List<Long> comments = recipe.getComments();
      if(!comments.contains(comment.getId())) {

          comments.add(comment.getId());
          recipe.setComments(comments);

          recipeRepository.save(recipe);
          recipeRepository.flush();
      } else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment already ");
      }

  }

  // Delete a comment from a recipe
  public void deleteComment (Recipe recipe, Comment comment) {

      List<Long> comments = recipe.getComments();

      if(comments.contains(comment.getId())) {

          comments.remove(comment.getId());
          recipe.setComments(comments);

          recipeRepository.save(recipe);
          recipeRepository.flush();
      } else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to recipe");
      }
  }

  // Vote on a recipe
  public void voteOnRecipe(Long recipeID, VotingDTO votingInput) {

    //check if recipe exists
    Recipe recipe = recipeRepository.findById(recipeID)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found, Comment could not be created"));

    //if recipe has no voting yet
    if (recipe.getCount() == null) {

        recipe.setCount(1);

        double doubleValue = votingInput.getVote().doubleValue(); // Extract the primitive double value
        Long newSum = (long) doubleValue;

        recipe.setSum(newSum);

        recipe.setVote(votingInput.getVote());
    }
    //recipe already has voting
    else {

        Integer newCount = recipe.getCount() + 1;
        recipe.setCount(newCount);

        double doubleValue = votingInput.getVote().doubleValue();
        Long summing = (long) doubleValue;

        Long newSum = recipe.getSum() + summing;
        recipe.setSum(newSum);

        Double sumDouble = recipe.getSum().doubleValue();
        Double countDouble = recipe.getCount().doubleValue();

        Double newVote = sumDouble / countDouble;
        Double roundedNum = Math.round(newVote * 2) / 2.0;

        recipe.setVote(roundedNum);
    }

    recipeRepository.save(recipe);
    recipeRepository.flush();
  }
}