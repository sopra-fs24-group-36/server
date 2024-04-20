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

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


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
      assertNull(userRepository.findByEmail("email"));

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
      assertNull(userRepository.findByEmail("email"));

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

    @Test
    public void createUser_duplicateEmail_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));
        assertNull(userRepository.findByEmail("email"));

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the email but forget about the username
        testUser2.setPassword("password");
        testUser2.setUsername("username2");
        testUser2.setEmail("email.email@email.com");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
    }


    //  test logIn method  //
    @Test
    public void logInUser_validInput() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        testUser = userRepository.save(testUser);
        userRepository.flush();

        // when
        User loggedInUser = userService.logIn(testUser);

        // then
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }


    @Test
    public void logInUser_InvalidPasswordAndUsername() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        userRepository.save(testUser);
        userRepository.flush();

        User wrongUser = new User();
        wrongUser.setId(1L);
        wrongUser.setPassword("wrong");
        wrongUser.setUsername("wrong");
        wrongUser.setEmail("email.email@email.com");
        wrongUser.setToken(UUID.randomUUID().toString());
        wrongUser.setCreationDate(creationDate);
        wrongUser.setStatus(UserStatus.OFFLINE);

        assertThrows(ResponseStatusException.class, () -> userService.logIn(wrongUser));
    }



    //  test logOut method //

    @Test
    public void logOutUser_validInput() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.ONLINE);

        testUser = userRepository.save(testUser);
        userRepository.flush();

        // when
        userService.logOut(testUser.getId());

        User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
        // then
        assertNotNull(updatedUser); // Ensure user exists
        assertEquals(UserStatus.OFFLINE, updatedUser.getStatus());
    }

    @Test
    public void logOutUser_InvalidId() {

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.ONLINE);

        userRepository.save(testUser);
        userRepository.flush();

        assertThrows(ResponseStatusException.class, () -> userService.logOut(2L));
    }





    //  test getTheUser method //
    @Test
    public void getTheUser_validInputs_success() {

      //given
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        // when
        User createdUser = userService.getTheUser(testUser.getId());

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void getTheUser_InvalidInputs_throwsException() {

        //given
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        // when
        assertThrows(ResponseStatusException.class, () -> userService.getTheUser(2L));
    }



    //  test updateTheUser method //
    @Test
    public void updateTheUser_validInputs_success() {

        //given
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        User updates = new User();
        updates.setPassword("new");
        updates.setUsername("new");
        updates.setEmail("new");
        updates.setProfilePicture("new");


        // when
        userService.updateTheUser(testUser.getId(), updates);

        // compare the updated email from email retrieved from database
        assertEquals(userRepository.findById(testUser.getId()).get().getEmail(), updates.getEmail());
        assertEquals(userRepository.findById(testUser.getId()).get().getUsername(), updates.getUsername());
        assertEquals(userRepository.findById(testUser.getId()).get().getName(), updates.getName());
        assertEquals(userRepository.findById(testUser.getId()).get().getProfilePicture(), updates.getProfilePicture());
    }


    @Test
    public void updateTheUser_InvalidUsername_throwsException() {

        //given
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        User updates = new User();
        updates.setPassword("new");
        updates.setUsername("username");
        updates.setEmail("new");
        updates.setProfilePicture("new");


        assertThrows(ResponseStatusException.class, () -> userService.updateTheUser(testUser.getId(), updates));

    }

    @Test
    public void updateTheUser_InvalidEmail_throwsException() {

        //given
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        User updates = new User();
        updates.setPassword("new");
        updates.setUsername("new");
        updates.setEmail("email.email@email.com");
        updates.setProfilePicture("new");


        assertThrows(ResponseStatusException.class, () -> userService.updateTheUser(testUser.getId(), updates));

    }

    @Test
    public void updateTheUser_InvalidId_throwsException() {

        //given
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setStatus(UserStatus.ONLINE);
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);

        userRepository.save(testUser);
        userRepository.flush();

        User updates = new User();
        updates.setPassword("new");
        updates.setUsername("new");
        updates.setEmail("new");
        updates.setProfilePicture("new");


        assertThrows(ResponseStatusException.class, () -> userService.updateTheUser(5L, updates));

    }




}
