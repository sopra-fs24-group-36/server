package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setName("name");
    testUser.setEmail("email.email@email.com");
    testUser.setPassword("password");
    testUser.setUsername("username");

    // when -> any object is being saved in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }


//  test createUser method  //
    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.ONLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void createUser_duplicateInputs_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }


    //  test logIn method  //





    //  test logOut method //



    //  test getTheUser method //
    @Test
    public void getTheUser_validInputs_success() {

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        User getUser = userService.getTheUser(1L);

        assertEquals(testUser.getId(), getUser.getId());
        assertEquals(testUser.getName(), getUser.getName());
        assertEquals(testUser.getUsername(), getUser.getUsername());
        assertEquals(testUser.getEmail(), getUser.getEmail());
        assertEquals(testUser.getPassword(), getUser.getPassword());
    }


    @Test
    public void getTheUser_InvalidInputs_throwsException() {

        Mockito.when(userRepository.findById(2L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.getTheUser(2L));
    }



    //  test updateTheUser method //
    @Test
    public void updateTheUser_validInputs_success() {

        User update = new User();
        update.setName("new");
        update.setEmail("new");
        update.setUsername("new");
        update.setProfilePicture("new");

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByEmail("new")).thenReturn(null);
        Mockito.when(userRepository.findByUsername("new")).thenReturn(null);


        userService.updateTheUser(1L, update);


        assertEquals(testUser.getName(), update.getName());
        assertEquals(testUser.getUsername(), update.getUsername());
        assertEquals(testUser.getEmail(), update.getEmail());
        assertEquals(testUser.getProfilePicture(), update.getProfilePicture());
    }


    @Test
    public void updateTheUser_InvalidInputs_throwsException() {

        User update = new User();
        update.setName("new");
        update.setEmail("email.email@email.com");
        update.setUsername("new");
        update.setProfilePicture("new");

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByEmail("email.email@email.com")).thenReturn(testUser);
        Mockito.when(userRepository.findByUsername("new")).thenReturn(null);


        assertThrows(ResponseStatusException.class, () -> userService.updateTheUser(1L, update));
    }


}
