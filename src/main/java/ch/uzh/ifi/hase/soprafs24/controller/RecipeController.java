package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RecipeController {

  private final RecipeService recipeService;

  RecipeController(RecipeService recipeService) {this.recipeService = recipeService; }

  //here come the post/get/put mappings
}
