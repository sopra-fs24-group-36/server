package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
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

  private CookbookRepository cookbookRepository;

  @Autowired
  public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  @Autowired
  public void CookbookService(@Qualifier("cookbookRepository") CookbookRepository cookbookRepository) {
    this.cookbookRepository = cookbookRepository;
  }

  public Recipe createUserRecipe(Long userID, Recipe newRecipe) {

    //something like checkifrecipeexists? probably not because it is in the users responsibility to not save multiple same recipes

    newRecipe = recipeRepository.save(newRecipe);

    newRecipe.setAuthorID(userID);
    
    //something to save the recipe into all the right cookbooks, this would be in a loop since there may be more cookbooks to save it to
    //maybe something like: for cookbookID in cookbooks {c = cookbookservice.findcookbook(cookbookID) then c.setrecipe(newrecipe) if the recipes are empty}
    List<Long> cookbookIDs = newRecipe.getCookbooks();
        
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

}