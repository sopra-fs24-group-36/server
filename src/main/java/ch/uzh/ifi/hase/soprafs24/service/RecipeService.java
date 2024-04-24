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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    
    List<Long> groupIDs = newRecipe.getGroups();

    User user = userRepository.findById(userID).orElse(null);

      if(user == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
      }

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

  public void deleteRecipe(Recipe recipe) {
    for (Long id:recipe.getGroups()){
      Group g = groupRepository.findById(id).orElse(null);
      if(g == null){throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");}
      Cookbook c = g.getCookbook();
      List<Long> recipes = c.getRecipes();
      if (recipes.contains(recipe.getId())){
        recipes.remove(recipe.getId());
        c.setRecipes(recipes);
        cookbookRepository.save(c);
      } else {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe is not part of one of these groups");}
    }
    recipeRepository.delete(recipe);
  }

  public Group removeRecipeFromGroup(Long groupID, Long recipeID) {
    Group group = groupRepository.findById(groupID).orElseThrow(() -> new RuntimeException("Group not found"));

    Cookbook cookbook = group.getCookbook();
    List<Long> recipes = cookbook.getRecipes();
    recipes.remove(recipeID);
    cookbook.setRecipes(recipes);

    cookbookRepository.save(cookbook);
    groupRepository.save(group);

    return group;
  }

}