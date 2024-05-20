package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
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

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private GroupRepository groupRepository;

  @InjectMocks
  private UserService userService;

  @Mock
  private CommentService commentService;

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
    @Test
    public void logIn_validInput () {

        // Arrange
        Mockito.when(userRepository.findByUsername("username")).thenReturn(testUser);
        Mockito.when(userRepository.findByPassword("password")).thenReturn(testUser);
        Mockito.when(userRepository.findByUsernameAndPassword("username", "password")).thenReturn(testUser);


        // Act
        User loggedInUser = userService.logIn(testUser);

        // Assert
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }


    @Test
    public void logIn_invalidUsernameAndPassword () {

        // Arrange
        Mockito.when(userRepository.findByUsername("username")).thenReturn(testUser);
        Mockito.when(userRepository.findByPassword("password")).thenReturn(testUser);
        Mockito.when(userRepository.findByUsernameAndPassword("username", "password")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.logIn(testUser));
    }


    //  test logOut method //
    @Test
    public void logOut_validInput () {

        // Arrange
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        // Act
        userService.logOut(1L);

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }


    @Test
    public void logOut_inValidId () {

        // Arrange
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.logOut(1L));
    }



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


    @Test
    public void userDeclinesInvitation_validInput_success() {

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        testUser.setInvitations(invitations);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        userService.userDeclinesInvitation(testUser.getId(), 2L);

        assertEquals(testUser.getInvitations(), new ArrayList<>());

    }

    @Test
    public void userDeclinesInvitation_inValidUser_throwsException() {

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        testUser.setInvitations(invitations);

        Mockito.when(userRepository.findById(3L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.userDeclinesInvitation(3L, 2L));
    }

    @Test
    public void userDeclinesInvitation_inValidInvitation_throwsException() {

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        testUser.setInvitations(invitations);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        assertThrows(ResponseStatusException.class, () -> userService.userDeclinesInvitation(testUser.getId(), 4L));
    }

    @Test
    public void userDeclinesInvitation_inValidInvitationNull_throwsException() {

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        assertThrows(ResponseStatusException.class, () -> userService.userDeclinesInvitation(testUser.getId(), 4L));
    }


    @Test
    public void userAcceptsInvitation_validInput_success() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);
        List<Long> members = new ArrayList<>();
        members.add(50L);
        group.setMembers(members);

        List<Long> invitations = new ArrayList<>();
        invitations.add(group.getId());
        testUser.setInvitations(invitations);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        userService.userAcceptsInvitation(testUser.getId(), group.getId());

        members.add(testUser.getId());
        groups.add(group.getId());

        assertEquals(new ArrayList<>(), testUser.getInvitations());
        assertEquals(groups, testUser.getGroups());
        assertEquals(members, group.getMembers());
    }


    @Test
    public void userAcceptsInvitation_inValidUser_throwsException() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);
        List<Long> members = new ArrayList<>();
        members.add(50L);
        group.setMembers(members);

        List<Long> invitations = new ArrayList<>();
        invitations.add(group.getId());
        testUser.setInvitations(invitations);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(3L)).thenReturn(null);
        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        assertThrows(ResponseStatusException.class, () -> userService.userAcceptsInvitation(3L, group.getId()));
    }

    @Test
    public void userAcceptsInvitation_inValidInvitationNull_throwsException() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);
        List<Long> members = new ArrayList<>();
        members.add(50L);
        group.setMembers(members);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        assertThrows(ResponseStatusException.class, () -> userService.userAcceptsInvitation(testUser.getId(), group.getId()));
    }

    @Test
    public void userAcceptsInvitation_inValidInvitation_throwsException() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);
        List<Long> members = new ArrayList<>();
        members.add(50L);
        group.setMembers(members);

        List<Long> invitations = new ArrayList<>();
        invitations.add(30L);
        testUser.setInvitations(invitations);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        assertThrows(ResponseStatusException.class, () -> userService.userAcceptsInvitation(testUser.getId(), group.getId()));
    }

    @Test
    public void userAcceptsInvitation_inValidGroup_throwsException() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);
        List<Long> members = new ArrayList<>();
        members.add(50L);
        group.setMembers(members);

        List<Long> invitations = new ArrayList<>();
        invitations.add(30L);
        testUser.setInvitations(invitations);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(groupRepository.findById(5L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.userAcceptsInvitation(testUser.getId(), 5L));
    }

    @Test
    public void userAcceptsInvitation_inValidMembersNull_throwsException() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);

        List<Long> invitations = new ArrayList<>();
        invitations.add(30L);
        testUser.setInvitations(invitations);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(groupRepository.findById(5L)).thenReturn(Optional.of(group));

        assertThrows(ResponseStatusException.class, () -> userService.userAcceptsInvitation(testUser.getId(), 5L));
    }


    @Test
    public void userAcceptsInvitation_inValidUserAlreadyInGroup_throwsException() {

        Group group = new Group();
        group.setName("name");
        group.setId(2L);
        List<Long> members = new ArrayList<>();
        members.add(testUser.getId());
        group.setMembers(members);

        List<Long> invitations = new ArrayList<>();
        invitations.add(30L);
        testUser.setInvitations(invitations);

        List<Long> groups = new ArrayList<>();
        groups.add(80L);
        testUser.setGroups(groups);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(groupRepository.findById(Mockito.any())).thenReturn(Optional.of(group));

        assertThrows(ResponseStatusException.class, () -> userService.userAcceptsInvitation(testUser.getId(), group.getId()));
    }


}
