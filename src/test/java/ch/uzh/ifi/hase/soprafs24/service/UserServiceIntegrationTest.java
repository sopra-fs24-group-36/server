package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }



    //  test the createUser method  //
  @Test
  public void createUser_validInputs_success() {
      // given
      assertNull(userRepository.findByUsername("username"));

      User testUser = new User();
      testUser.setPassword("password");
      testUser.setUsername("username");
      testUser.setEmail("email.email@email.com");

      // when
      User createdUser = userService.createUser(testUser);

      // then
      assertEquals(testUser.getId(), createdUser.getId());
      assertEquals(testUser.getEmail(), createdUser.getEmail());
      assertEquals(testUser.getUsername(), createdUser.getUsername());
      assertNotNull(createdUser.getToken());
      assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setPassword("password");
      testUser.setUsername("username");
      testUser.setEmail("email.email@email.com");
      User createdUser = userService.createUser(testUser);

      // attempt to create second user with same username
      User testUser2 = new User();

      // change the email but forget about the username
      testUser2.setPassword("password");
      testUser2.setUsername("username");
      testUser2.setEmail("email2.email2@email2.com");

      // check that an error is thrown
      assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }


    //  test logIn method  //





    //  test logOut method //

}
