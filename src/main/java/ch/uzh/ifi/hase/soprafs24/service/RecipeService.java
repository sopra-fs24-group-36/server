package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class RecipeService {

  private final Logger log = LoggerFactory.getLogger(RecipeService.class);

  private final RecipeRepository recipeRepository;

  @Autowired
  public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }


  public Recipe createRecipe(Recipe newRecipe) {

    newRecipe = recipeRepository.save(newRecipe);
    recipeRepository.flush();

    log.debug("Created new Recipe: {}", newRecipe);

    return newRecipe;
  }

}