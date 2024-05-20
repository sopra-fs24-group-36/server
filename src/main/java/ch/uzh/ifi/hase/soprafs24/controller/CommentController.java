package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
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

// Controller to handle all comment related endpoints
@RestController
public class CommentController {

  private final CommentService commentService;

  private final RecipeService recipeService;

  private final RecipeRepository recipeRepository;

  private final CommentRepository commentRepository;


    CommentController(CommentService commentService, RecipeService recipeService, RecipeRepository recipeRepository, CommentRepository commentRepository)
  {
      this.commentService = commentService;
      this.recipeService = recipeService;
      this.recipeRepository = recipeRepository;
      this.commentRepository = commentRepository;
  }

    // Create a comments of a single recipe
    @PostMapping("/comments/recipes/{recipeID}")
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
      String text = createdComment.getText().trim();
      if (!text.isEmpty()) {
          recipeService.addComment(recipeID, createdComment);
      } else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "text needs to have at least length 1");
      }

      return DTOMapper.INSTANCE.convertEntityToCommentDTO(createdComment);

  }

    // Update comment of a single recipe
    @PutMapping("/comments/{commentID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void updateComment(@RequestBody CommentPutDTO commentPutDTO, @PathVariable Long commentID) {

        Comment commentUpdate = DTOMapper.INSTANCE.convertCommentPutDTOtoEntity(commentPutDTO);

        //change comment
        commentService.updateComment(commentID, commentUpdate);
    }


    // Delete comment of a single recipe
    @DeleteMapping("/comments/{commentID}/recipes/{recipeID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteComment (@PathVariable Long commentID, @PathVariable Long recipeID) {

        //check that comment exists
        Comment comment = commentRepository.findById(commentID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        //check that recipe exists
        Recipe recipe = recipeRepository.findById(recipeID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        //delete comment out of comment repository
        commentService.deleteComment(comment);

        //delete comment out of recipe comment list
        recipeService.deleteComment(recipe, comment);

    }
  
}
