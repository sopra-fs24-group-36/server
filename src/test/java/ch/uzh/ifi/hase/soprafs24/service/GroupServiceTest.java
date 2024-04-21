package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;

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

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));

        groupService.addUserToGroup(1L, testGroup.getId());

        Mockito.verify(groupRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    public void addUserToGroup_inValidGroupId_throwsException() {

        Mockito.when(groupRepository.findById(5L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> groupService.addUserToGroup(1L, 5L));
    }


    //  test deleteUserFromGroup method //
    @Test
    public void deleteUserFromGroup_validInputs_success() {

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));

        groupService.deleteUserFromGroup(testGroup.getId(), 789L);

        Mockito.verify(groupRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    public void deleteUserFromGroup_inValidUserId_throwsException() {

        Mockito.when(groupRepository.findById(1L)).thenReturn(Optional.ofNullable(testGroup));

        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(testGroup.getId(), 2L));
    }


    @Test
    public void deleteUserFromGroup_inValidGroupId_throwsException() {

        Mockito.when(groupRepository.findById(5L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup( 5L, 789L));
    }
}