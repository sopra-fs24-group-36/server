package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CommentRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CommentService commentService;

  private Comment testComment;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    testComment = new Comment();
    testComment.setId(1L);
    testComment.setUsername("username");
    testComment.setText("text");
    testComment.setUserID(3L);

    Mockito.when(commentRepository.save(Mockito.any())).thenReturn(testComment);
  }

    //  createComment   //
    @Test
    public void createComment_validInput_success() {

        User testUser = new User();
        testUser = new User();
        testUser.setId(3L);
        testUser.setName("name");
        testUser.setEmail("email.email@email.com");
        testUser.setPassword("password");
        testUser.setUsername("username");

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        Comment createdComment = commentService.createComment(testComment);

        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testComment.getId(), createdComment.getId());
        assertEquals(testComment.getText(), createdComment.getText());
        assertEquals(testComment.getUserID(), createdComment.getUserID());
        assertEquals(testComment.getUsername(), createdComment.getUsername());
    }

    @Test
    public void createComment_UserNotFound_throwsException() {

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> commentService.createComment(testComment));
    }

    //  updateComment   //
    @Test
    public void updateComment_validInput_success() {

        Mockito.when(commentRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testComment));

        Comment comment = new Comment();
        comment.setText("newText");

        commentService.updateComment(testComment.getId(), comment);

        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void updateComment_CommentNotFound_throwsException() {

        Mockito.when(commentRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Comment comment = new Comment();
        comment.setText("newText");

        assertThrows(ResponseStatusException.class, () -> commentService.updateComment(2L, comment));
    }

    @Test
    public void updateComment_EmptyText_throwsException() {

        Mockito.when(commentRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testComment));

        Comment comment = new Comment();
        comment.setText("    ");

        assertThrows(ResponseStatusException.class, () -> commentService.updateComment(2L, comment));
    }


    //  deleteComment   //
    @Test
    public void deleteComment_validInput_success() {

        commentService.deleteComment(testComment);

        Mockito.verify(commentRepository, Mockito.times(1)).delete(Mockito.any());
    }


    //  changeUsername   //
    @Test
    public void changeUsername_validInput_success() {

        User testUser = new User();
        testUser = new User();
        testUser.setId(3L);
        testUser.setName("name");
        testUser.setEmail("email.email@email.com");
        testUser.setPassword("password");
        testUser.setUsername("username");

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        List<Comment> comments = new ArrayList<>();
        comments.add(testComment);
        Mockito.when(commentRepository.findByUserID(Mockito.any())).thenReturn(comments);

        commentService.changeUsername(testComment.getUserID());

        Mockito.verify(commentRepository, Mockito.times(1)).save(Mockito.any());
    }
}
