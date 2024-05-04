package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
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


@RestController
public class CommentController {

  private final CommentService commentService;

  private final RecipeService recipeService;


    CommentController(CommentService commentService, RecipeService recipeService)
  {
      this.commentService = commentService;
      this.recipeService = recipeService;
  }


    @PostMapping("/comments/{recipeID}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public CommentDTO createComment(@RequestBody CommentPostDTO commentPostDTO, @PathVariable Long recipeID) {

      Comment commentInput = DTOMapper.INSTANCE.convertCommentPostDTOtoEntity(commentPostDTO);

      //create comment and save it
      Comment createdComment = commentService.createComment(commentInput);


      //save comment to recipe it belongs to
      recipeService.addComment(recipeID, createdComment);

      return DTOMapper.INSTANCE.convertEntityToCommentDTO(createdComment);

  }
  
}
