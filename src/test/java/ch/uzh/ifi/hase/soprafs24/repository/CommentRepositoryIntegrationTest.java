package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CommentRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private CommentRepository commentRepository;


  // test findByUserID //
  @Test
  public void findByUserID_success() {
      // given
      Comment comment = new Comment();
      comment.setUsername("username");
      comment.setText("text");
      comment.setUserID(3L);

      entityManager.persist(comment);
      entityManager.flush();

      // when
      List<Comment> found = commentRepository.findByUserID(comment.getId());

      // then
      for (Comment com : found) {
          assertNotNull(com.getId());
          assertEquals(com.getText(), comment.getText());
          assertEquals(com.getUsername(), comment.getUsername());
      }
    }


}
