package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import org.springframework.web.bind.annotation.*;

@RestController
public class CookbookController {

    private final CookbookService cookbookService;

    CookbookController(CookbookService cookbookService) {this.cookbookService = cookbookService; }

    
}
