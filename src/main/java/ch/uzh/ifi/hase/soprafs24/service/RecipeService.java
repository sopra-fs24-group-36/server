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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import antlr.collections.Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@Service
@Transactional
public class RecipeService {

  private final Logger log = LoggerFactory.getLogger(RecipeService.class);

  private final RecipeRepository recipeRepository;

  private final CookbookRepository cookbookRepository;

  private final GroupRepository groupRepository;

  private final UserRepository userRepository;

  @Autowired
  public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository, CookbookRepository cookbookRepository, GroupRepository groupRepository, UserRepository userRepository) {
    this.recipeRepository = recipeRepository;
    this.cookbookRepository = cookbookRepository;
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
  }

  public Recipe createUserRecipe(Long userID, Recipe newRecipe) {

    newRecipe = recipeRepository.save(newRecipe);

    //something like checkifrecipeexists? probably not because it is in the users responsibility to not save multiple same recipes 
    newRecipe.setAuthorID(userID);

    User author = userRepository.findById(userID).orElse(null);
    if (author == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found!");
    }
    
    Cookbook perscookbook = author.getCookbook();
    List<Long> persrecipes = perscookbook.getRecipes();
    persrecipes.add(newRecipe.getId());
    perscookbook.setRecipes(persrecipes);
    cookbookRepository.save(perscookbook);
    
    //something to save the recipe into all the right cookbooks, this would be in a loop since there may be more cookbooks to save it to
    //maybe something like: for cookbookID in cookbooks {c = cookbookservice.findcookbook(cookbookID) then c.setrecipe(newrecipe) if the recipes are empty}
    List<Long> cookbookIDs = newRecipe.getCookbooks();
    cookbookIDs.add(perscookbook.getId());
        
    // Loop through each cookbook ID
    for (long cookbookID : cookbookIDs) {
      Cookbook c = cookbookRepository.findById(cookbookID);
      List<Long> recipes = c.getRecipes();
      recipes.add(newRecipe.getId()); // Add the new recipe to the list of recipes in the cookbook
      c.setRecipes(recipes);
    }
    cookbookRepository.flush();
    recipeRepository.flush();

    log.debug("Created new Recipe: {}", newRecipe);

    return newRecipe;
  }

  public Recipe createGroupRecipe(Long groupID, Recipe newRecipe) {

    // Save the new recipe
    newRecipe = recipeRepository.save(newRecipe);

    // Get the IDs of the cookbooks to associate the recipe with
    List<Long> cookbookIDs = newRecipe.getCookbooks();
        
    // Loop through each cookbook ID
    for (long cookbookID : cookbookIDs) {
        // Find the cookbook
        Cookbook c = cookbookRepository.findById(cookbookID);
        if (c != null) {
            // Add the new recipe to the list of recipes in the cookbook
            List<Long> recipes = c.getRecipes();
            recipes.add(newRecipe.getId());
            c.setRecipes(recipes);
            // Save the updated cookbook
            cookbookRepository.save(c);
        } else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong, please check content");
        }
    }

    // Flush changes to the database
    cookbookRepository.flush();
    recipeRepository.flush();

    log.debug("Created new Recipe: {}", newRecipe);

    return newRecipe;
}


  public Recipe findRecipeById(Long recipeID) {
    Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
    return optionalRecipe.orElse(null); // Returns null if recipe is not found
  }

  public List<Recipe> findAllRecipesWithUserID(Long userID) {
    List<Recipe> recipes = recipeRepository.findByAuthorID(userID);

    return recipes;
  }

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

    List<Long> cookbooks = recipeToUpdate.getCookbooks();

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
      if (cookbooks != null) {
        List<Long> before = recipe.getCookbooks();
        recipe.setCookbooks(cookbooks);
        List<Long> after = recipe.getCookbooks();
    
        // Create copies of the lists to avoid modifying the originals
        List<Long> beforeCopy = new ArrayList<>(before);
        List<Long> afterCopy = new ArrayList<>(after);
    
        // Calculate the elements present in 'after' but not in 'before'
        afterCopy.removeAll(before);

        for (long cookbookID:afterCopy){
          //add recipe to cookbook
          Cookbook c = cookbookRepository.findById(cookbookID);
          List<Long> recipes = c.getRecipes();
          recipes.add(recipe.getId()); // Add the new recipe to the list of recipes in the cookbook
          c.setRecipes(recipes);
        }
    
        // Calculate the elements present in 'before' but not in 'after'
        beforeCopy.removeAll(after);

        for (long cookbookID:beforeCopy){
          //remove elements from cookbooks
          Cookbook c = cookbookRepository.findById(cookbookID);
          List<Long> recipes = c.getRecipes();
          recipes.remove(recipe.getId()); // Add the new recipe to the list of recipes in the cookbook
          c.setRecipes(recipes);
        }

        recipeRepository.save(recipe);
        recipeRepository.flush();
      }
      
  
    } catch(Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong, please check content");
    }

  }

  public void deleteRecipe(Recipe recipe) {
    for (Long cookbookID : recipe.getCookbooks()) {
        Optional<Cookbook> cOptional = cookbookRepository.findById(cookbookID);
        cOptional.ifPresent(c -> {
            List<Long> recipes = c.getRecipes();
            if (recipes != null && recipes.contains(recipe.getId())) {
                recipes.remove(recipe.getId());
                c.setRecipes(recipes);
                cookbookRepository.save(c);
            }
        });
    }
    recipeRepository.delete(recipe);
  }

  public void removeRecipeFromGroup(Long groupID, Long recipeID) {
    Group group = groupRepository.findById(groupID).orElseThrow(() -> new RuntimeException("Group not found"));

    Cookbook cookbook = group.getCookbook();
    List<Long> recipes = cookbook.getRecipes();
    recipes.remove(recipeID);
    cookbook.setRecipes(recipes);

    cookbookRepository.save(cookbook);
    groupRepository.save(group);
  }

}