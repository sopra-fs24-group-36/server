package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class CommentController {

  private final CommentService commentService;

  private final RecipeService recipeService;

  private final RecipeRepository recipeRepository;


    CommentController(CommentService commentService, RecipeService recipeService, RecipeRepository recipeRepository)
  {
      this.commentService = commentService;
      this.recipeService = recipeService;
      this.recipeRepository = recipeRepository;
  }


    @PostMapping("/comments/{recipeID}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CommentDTO createComment(@RequestBody CommentPostDTO commentPostDTO, @PathVariable Long recipeID) {

      Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);

      Optional<Recipe> recipeOptional = recipeRepository.findById(recipeID);
      if (!recipeOptional.isPresent()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
      }

      //create comment and save it
      Comment createdComment = commentService.createComment(commentInput);

      //save comment to recipe it belongs to
      recipeService.addComment(recipeID, createdComment);

      return DTOMapper.INSTANCE.convertEntityToCommentDTO(createdComment);

  }
  
}
