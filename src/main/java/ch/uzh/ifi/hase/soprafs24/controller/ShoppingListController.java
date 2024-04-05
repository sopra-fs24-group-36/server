package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShoppingListController {

  private final ShoppingListService shoppingListService;

  ShoppingListController(ShoppingListService shoppingListService) {this.shoppingListService = shoppingListService; }

  //here come the post/get/put mappings
}
