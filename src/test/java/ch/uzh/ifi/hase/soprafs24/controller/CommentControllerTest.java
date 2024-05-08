package ch.uzh.ifi.hase.soprafs24.controller;


import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CommentPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.CommentService;
import ch.uzh.ifi.hase.soprafs24.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @MockBean
  private RecipeService recipeService;

  @MockBean
  private RecipeRepository recipeRepository;

  @MockBean
  private CommentRepository commentRepository;


  //    createComment   //
  @Test
  public void createComment_validInput_commentCreated() throws Exception {

      CommentPostDTO commentPostDTO = new CommentPostDTO();
      commentPostDTO.setText("example");
      commentPostDTO.setUserID(2L);

      Comment comment = new Comment();
      comment.setId(3L);
      comment.setUsername("Barbara");
      comment.setText("example");
      comment.setUserID(2L);

      Recipe recipe = new Recipe();
      recipe.setId(1L);
      recipe.setTitle("testRecipeName");
      recipe.setShortDescription("testDescription");
      recipe.setLink("testLink");
      recipe.setCookingTime("testTime");

      given(recipeRepository.findById(recipe.getId())).willReturn(Optional.of(recipe));
      given(commentService.createComment(Mockito.any(Comment.class))).willReturn(comment);

      MockHttpServletRequestBuilder postRequest = post("/comments/recipes/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(commentPostDTO));

      mockMvc.perform(postRequest)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.text", is(comment.getText())))
              .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
              .andExpect(jsonPath("$.username", is(comment.getUsername())));

  }


    @Test
    public void createComment_inValidInput_RecipeNotFound() throws Exception {

        CommentPostDTO commentPostDTO = new CommentPostDTO();
        commentPostDTO.setText("example");
        commentPostDTO.setUserID(2L);

        Comment comment = new Comment();
        comment.setId(3L);
        comment.setUsername("Barbara");
        comment.setText("example");
        comment.setUserID(2L);


        given(recipeRepository.findById(Mockito.anyLong())).willThrow(new IllegalStateException("Recipe not found"));
        given(commentService.createComment(Mockito.any(Comment.class))).willReturn(comment);

        MockHttpServletRequestBuilder postRequest = post("/comments/recipes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(commentPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());

    }

  //    updateComment    //
  @Test
  public void updateComment_validInput_commentUpdated() throws Exception {

      CommentPutDTO commentPutDTO = new CommentPutDTO();
      commentPutDTO.setText("newText");

      //given(commentService.createComment(Mockito.any(Comment.class))).willReturn(comment);

      MockHttpServletRequestBuilder putRequest = put("/comments/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(commentPutDTO));

      mockMvc.perform(putRequest)
              .andExpect(status().isOk());

  }


  //    deleteComment   //
  @Test
  public void deleteComment_validInput_commentDeleted() throws Exception {

      Comment comment = new Comment();
      comment.setId(3L);
      comment.setUsername("Barbara");
      comment.setText("example");
      comment.setUserID(2L);

      Recipe recipe = new Recipe();
      recipe.setId(1L);
      recipe.setTitle("testRecipeName");
      recipe.setShortDescription("testDescription");
      recipe.setLink("testLink");
      recipe.setCookingTime("testTime");

      given(recipeRepository.findById(recipe.getId())).willReturn(Optional.of(recipe));
      given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

      MockHttpServletRequestBuilder deleteRequest = delete("/comments/3/recipes/1")
              .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(deleteRequest)
              .andExpect(status().isOk());

  }


    @Test
    public void deleteComment_inValidInput_RecipeNotFound() throws Exception {

        Comment comment = new Comment();
        comment.setId(3L);
        comment.setUsername("Barbara");
        comment.setText("example");
        comment.setUserID(2L);


        given(recipeRepository.findById(Mockito.anyLong())).willThrow(new IllegalStateException("Recipe not found"));
        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        MockHttpServletRequestBuilder deleteRequest = delete("/comments/3/recipes/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());

    }


  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}