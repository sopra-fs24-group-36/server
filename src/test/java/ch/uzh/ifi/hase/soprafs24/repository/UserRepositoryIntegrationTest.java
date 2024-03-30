package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;


  // test findByUsername //
  @Test
  public void findByUsername_success() {
      // given
      User user = new User();
      user.setPassword("password");
      user.setUsername("username");
      user.setStatus(UserStatus.ONLINE);
      user.setToken("1");
      user.setEmail("email.email@email.com");
      Date creationDate = new Date();
      user.setCreationDate(creationDate);


      entityManager.persist(user);
      entityManager.flush();

      // when
      User found = userRepository.findByUsername(user.getUsername());

      // then
      assertNotNull(found.getId());
      assertEquals(found.getUsername(), user.getUsername());
      assertEquals(found.getToken(), user.getToken());
      assertEquals(found.getStatus(), user.getStatus());
      assertEquals(found.getPassword(), user.getPassword());
      assertEquals(found.getEmail(), user.getEmail());
      assertNotNull(found.getCreationDate());
    }


    // test findByEmail //
    @Test
    public void findByEmail_success() {
        // given
        User user = new User();
        user.setPassword("password");
        user.setUsername("username");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setEmail("email.email@email.com");
        Date creationDate = new Date();
        user.setCreationDate(creationDate);


        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByEmail(user.getEmail());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getEmail(), user.getEmail());
        assertNotNull(found.getCreationDate());
    }
}
