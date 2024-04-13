package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.CookbookRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

  public Recipe createRecipe(Long userID, Recipe newRecipe) {

    //something like checkifrecipeexists? probably not because it is in the users responsibility to not save multiple same recipes

    newRecipe = recipeRepository.save(newRecipe);

    newRecipe.setAuthorID(userID);
    
    //something to save the recipe into all the right cookbooks, this would be in a loop since there may be more cookbooks to save it to
    //maybe something like: for cookbookID in cookbooks {c = cookbookservice.findcookbook(cookbookID) then c.setrecipe(newrecipe) if the recipes are empty}
    List<Long> cookbookIDs = newRecipe.getCookbooks();
        
    // Loop through each cookbook ID
    for (long cookbookID : cookbookIDs) {
      Cookbook c = cookbookRepository.findById(cookbookID);
      List<Long> recipes = c.getRecipes(); // Assuming getRecipes() returns a list of recipes in the cookbook
      recipes.add(cookbookID); // Add the new recipe to the list of recipes in the cookbook
    }

    recipeRepository.flush();

    log.debug("Created new Recipe: {}", newRecipe);

    return newRecipe;
  }

  public Recipe findRecipeById(Long recipeID) {
    Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeID);
    return optionalRecipe.orElse(null); // Returns null if recipe is not found
  }

}