package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebAppConfiguration
@SpringBootTest
public class GroupServiceIntegrationTest {

    @Qualifier("groupRepository")
    @Autowired
    private GroupRepository groupRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupService groupService;

    @BeforeEach
    public void setup() {
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }


    //  test createGroup method //
    @Test
    public void createGroup_validInputs_success() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        // when
        Group createdGroup = groupService.createGroup(testGroup);

        // then
        assertEquals(testGroup.getId(), createdGroup.getId());
        assertEquals(testGroup.getName(), createdGroup.getName());
        assertEquals(testGroup.getMembers(), createdGroup.getMembers());

    }


    //  test saveCookbook method    //
    @Test
    public void saveCookbook_validInputs_success() {

        Group testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        Cookbook testCookbook = new Cookbook();
        testCookbook.setId(2L);
        testCookbook.setStatus(CookbookStatus.GROUP);

        // when
        groupService.saveCookbook(testGroup, testCookbook);

        // then
        assertEquals(testGroup.getCookbook(), testCookbook);

    }


    //  test addUserToGroup method  //
    @Test
    public void addUserToGroup_validInputs_success() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(790L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        groupRepository.save(testGroup);
        groupRepository.flush();

        // Call the method under test
        Group gotGroup = groupService.addUserToGroup(489L, testGroup.getId());

        List<Long> expectedMembers = new ArrayList<>(initialMembers);
        expectedMembers.add(489L);

        // Assert that the actual list of members matches the expected list
        assertEquals(expectedMembers, gotGroup.getMembers());
        assertTrue(gotGroup.getMembers().contains(789L));
        assertTrue(gotGroup.getMembers().contains(790L));
        assertTrue(gotGroup.getMembers().contains(489L));
    }


    @Test
    public void addUserToGroup_inValidGroupId_throwsException() {

        assertThrows(RuntimeException.class, () -> groupService.addUserToGroup(489L, 1L));

    }


    //  test deleteUserFromGroup method //
    @Test
    public void deleteUserFromGroup_validInputs_success() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(790L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        groupRepository.save(testGroup);
        groupRepository.flush();

        // Call the method under test
        Group gotGroup = groupService.deleteUserFromGroup(testGroup.getId(), 790L);

        List<Long> expectedMembers = new ArrayList<>(initialMembers);
        expectedMembers.remove(790L);

        // Assert that the actual list of members matches the expected list
        assertEquals(expectedMembers, gotGroup.getMembers());
        assertTrue(gotGroup.getMembers().contains(789L));
        assertFalse(gotGroup.getMembers().contains(790L));
    }


    @Test
    public void deleteUserFromGroup_inValidUserId_throwsException() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(790L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        groupRepository.save(testGroup);
        groupRepository.flush();

        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(testGroup.getId(), 800L));

    }


    @Test
    public void deleteUserFromGroup_inValidGroupId_throwsException() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(790L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        groupRepository.save(testGroup);
        groupRepository.flush();

        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(8L, 790L));

    }
}
