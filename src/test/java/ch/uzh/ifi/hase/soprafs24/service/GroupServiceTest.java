package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CookbookService cookbookService;

    @Mock
    private ShoppingListService shoppingListService;

    @InjectMocks
    private GroupService groupService;

    private Group testGroup;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        Mockito.when(groupRepository.save(Mockito.any())).thenReturn(testGroup);
    }

    //  test createGroup method //
    @Test
    public void createGroup_validInputs_success() {

        Long creator = 1L;

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setGroups(new ArrayList<>());

        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("testt");
        testUser2.setUsername("testt");
        testUser2.setEmail("testt");
        testUser2.setToken(UUID.randomUUID().toString());
        Date creationDate2 = new Date();
        testUser2.setCreationDate(creationDate2);
        testUser2.setStatus(UserStatus.OFFLINE);
        testUser2.setGroups(new ArrayList<>());
        testUser2.setInvitations(new ArrayList<>());


        List<String> list = new ArrayList<>();
        list.add("test");
        testGroup.setMembersNames(list);


        Mockito.when(userRepository.findById(creator)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.findByEmail("test")).thenReturn(testUser2);

        Group createdGroup = groupService.createGroup(creator, testGroup);

        Mockito.verify(groupRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testGroup.getId(), createdGroup.getId());
        assertEquals(testGroup.getName(), createdGroup.getName());
        assertEquals(testGroup.getMembers(), createdGroup.getMembers());
    }

    @Test
    public void createGroup_invalidInputs_UserNotFound() {

        Long creator = 1L;

        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setGroups(new ArrayList<>());

        List<String> list = new ArrayList<>();
        list.add("test");
        testGroup.setMembersNames(list);

        Mockito.when(userRepository.findById(creator)).thenReturn(Optional.of(testUser));
        Mockito.when(userRepository.findByEmail("test")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> groupService.createGroup(1L, testGroup));
    }

    @Test
    public void createGroup_invalidInputs_throwsException() {

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> groupService.createGroup(1L, testGroup));
    }

    //  test addUserToGroup method  //
    @Test
    public void addUserToGroup_validInputs_success() {

        User testUser = new User();
        testUser.setId(3L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        List<Long> initialGroups = new ArrayList<>();
        initialGroups.add(89L);
        testUser.setGroups(new ArrayList<>(initialGroups));

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        groupService.addUserToGroup(3L, 1L);

        Mockito.verify(groupRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    public void addUserToGroup_inValidGroupId_throwsException() {

        User testUser = new User();
        testUser.setId(3L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        List<Long> initialGroups = new ArrayList<>();
        initialGroups.add(89L);
        testUser.setGroups(new ArrayList<>(initialGroups));

        Mockito.when(groupRepository.findById(5L)).thenReturn(null);
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));


        assertThrows(RuntimeException.class, () -> groupService.addUserToGroup(3L, 5L));
    }


    @Test
    public void addUserToGroup_inValidUserId_throwsException() {

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));
        Mockito.when(userRepository.findById(8L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> groupService.addUserToGroup(8L, 1L));

    }


    //  test deleteUserFromGroup method //
    @Test
    public void deleteUserFromGroup_validInputs_success() {

        User testUser = new User();
        testUser.setId(789L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        List<Long> initialGroups = new ArrayList<>();
        initialGroups.add(89L);
        testUser.setGroups(new ArrayList<>(initialGroups));


        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));


        groupService.deleteUserFromGroup(789L, 1L);

        Mockito.verify(groupRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());


    }

    @Test
    public void deleteUserFromGroup_inValidUserId_throwsException() {

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));
        Mockito.when(userRepository.findById(5L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(5L, 1L));
    }


    @Test
    public void deleteUserFromGroup_inValidGroupId_throwsException() {

        User testUser = new User();
        testUser.setId(789L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        List<Long> initialGroups = new ArrayList<>();
        initialGroups.add(89L);
        testUser.setGroups(new ArrayList<>(initialGroups));


        Mockito.when(groupRepository.findById(5L)).thenReturn(null);
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));


        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup( 789L, 5L));
    }

    @Test
    public void inviteUserToGroup_validInput_success() {

        // create a user which will get invited
        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);

        List<Long> initialGroups = new ArrayList<>();
        testUser.setGroups(new ArrayList<>(initialGroups));
    
        List<Long> invitations = new ArrayList<>();
        testUser.setInvitations(invitations);

        // create the UserPostDTO object which the frontend sends to invite this user
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("username");

        // mock the finding-group-by-id function to return the created testgroup
        Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testGroup));

        // mock the finding-user-by-id function to return the created user
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(testUser);

        // asserting that the invitation is not there before the invitation is sent
        assertFalse(invitations.contains(testGroup.getId()));

        // do the action of inviting the user
        groupService.inviteUserToGroup(testGroup.getId(), userPostDTO);

        // asserting that the groupid is saved in the invitations of the user
        assertTrue(testUser.getInvitations().contains(testGroup.getId()));
    }

    @Test
    public void inviteUserToGroup_invalidInput_UserNotFound() {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("username");

        Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testGroup));
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        assertThrows(RuntimeException.class, () -> groupService.inviteUserToGroup(testGroup.getId(), userPostDTO));
    }

    @Test
    public void inviteUserToGroup_invalidInput_GroupNotFound() {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setEmail("username");

        Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> groupService.inviteUserToGroup(testGroup.getId(), userPostDTO));
    }
}