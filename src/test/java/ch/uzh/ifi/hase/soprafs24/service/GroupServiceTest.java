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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

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

        Group createdGroup = groupService.createGroup(testGroup);

        Mockito.verify(groupRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testGroup.getId(), createdGroup.getId());
        assertEquals(testGroup.getName(), createdGroup.getName());
        assertEquals(testGroup.getMembers(), createdGroup.getMembers());

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
}