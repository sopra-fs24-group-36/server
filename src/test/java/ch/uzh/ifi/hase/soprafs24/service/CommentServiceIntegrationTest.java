package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.Recipe;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class CommentServiceIntegrationTest {

    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Qualifier("recipeRepository")
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CommentService commentService;

    @BeforeEach
    public void setup() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        recipeRepository.deleteAll();
     }



    //  createComment   //
    @Test
    public void createComment_validInputs_success() {

        Recipe testRecipe = new Recipe();
        testRecipe.setTitle("test");
        testRecipe.setShortDescription("test");
        testRecipe.setLink("test");
        testRecipe.setCookingTime("test");
        testRecipe.setImage("test");
        testRecipe.setAuthorID(5L);

        recipeRepository.save(testRecipe);
        recipeRepository.flush();

        User testUser = new User();
        testUser = new User();
        testUser.setName("name");
        testUser.setEmail("email.email@email.com");
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        Comment testComment = new Comment();
        testComment.setUserID(testUser.getId());
        testComment.setText("testText");

        Comment createdComment = commentService.createComment(testComment);

        assertEquals(testComment.getId(), createdComment.getId());
        assertEquals(testComment.getUserID(), createdComment.getUserID());
        assertEquals(createdComment.getUsername(), "username");
        assertEquals(createdComment.getText(), "testText");
    }

    @Test
    public void createComment_inValidInputs_userNotFound() {

        Recipe testRecipe = new Recipe();
        testRecipe.setTitle("test");
        testRecipe.setShortDescription("test");
        testRecipe.setLink("test");
        testRecipe.setCookingTime("test");
        testRecipe.setImage("test");
        testRecipe.setAuthorID(5L);

        recipeRepository.save(testRecipe);
        recipeRepository.flush();


        Comment testComment = new Comment();
        testComment.setUserID(5L);
        testComment.setText("testText");

        assertThrows(ResponseStatusException.class, () -> commentService.createComment(testComment));
    }

    //  updateComment   //
    @Test
    public void updateComment_validInputs_success() {

        Comment testComment = new Comment();
        testComment.setUserID(4L);
        testComment.setUsername("andrea");
        testComment.setText("testText");

        commentRepository.save(testComment);
        commentRepository.flush();

        Comment commentUpdate = new Comment();
        commentUpdate.setText("newText");

        commentService.updateComment(testComment.getId(), commentUpdate);
        
        Optional<Comment> c = commentRepository.findById(testComment.getId());

        assertEquals("newText", c.get().getText());
    }

    @Test
    public void updateComment_inValidInputs_CommentNotFound() {

        Comment commentUpdate = new Comment();
        commentUpdate.setText("newText");

        assertThrows(ResponseStatusException.class, () -> commentService.updateComment(5L, commentUpdate));
    }

    @Test
    public void updateComment_inValidInputs_EmptyString() {

        Comment testComment = new Comment();
        testComment.setUserID(4L);
        testComment.setUsername("andrea");
        testComment.setText("testText");

        commentRepository.save(testComment);
        commentRepository.flush();

        Comment commentUpdate = new Comment();
        commentUpdate.setText("    ");

        assertThrows(ResponseStatusException.class, () -> commentService.updateComment(testComment.getId(), commentUpdate));
    }

    //  deleteComment   //
    @Test
    public void deleteComment_validInputs_success() {

        Comment testComment = new Comment();
        testComment.setUserID(4L);
        testComment.setUsername("andrea");
        testComment.setText("testText");

        commentRepository.save(testComment);
        commentRepository.flush();

        Long testId = testComment.getId();

        commentService.deleteComment(testComment);

        assertEquals(Optional.empty(), commentRepository.findById(testId));
    }

    @Test
    public void changeUsername_validInputs_success() {

        //create user
        User testUser = new User();
        testUser.setName("name");
        testUser.setEmail("email.email@email.com");
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        Long testId = testUser.getId();

        Comment testComment = new Comment();
        testComment.setUserID(testId);
        testComment.setUsername("andrea");
        testComment.setText("testText");

        commentRepository.save(testComment);
        commentRepository.flush();

        commentService.changeUsername(testId);

        Optional<Comment> c = commentRepository.findById(testComment.getId());

        assertEquals("username", c.get().getUsername());

    }
}
