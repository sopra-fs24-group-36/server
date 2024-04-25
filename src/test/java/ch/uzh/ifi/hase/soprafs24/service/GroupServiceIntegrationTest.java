package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

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


    /*
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
    */


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

    //  test saveShoppingList method    //
    @Test
    public void saveShoppingList_validInputs_success() {

        Group testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setId(2L);

        // when
        groupService.saveShoppingList(testGroup, shoppingList);

        // then
        assertEquals(testGroup.getShoppingList(), shoppingList);
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
        testGroup = groupRepository.save(testGroup);
        groupRepository.flush();

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser = userRepository.save(testUser);
        userRepository.flush();

        // Call the method under test
        Group gotGroup = groupService.addUserToGroup(testUser.getId(), testGroup.getId());

        List<Long> expectedMembers = new ArrayList<>(initialMembers);
        expectedMembers.add(testUser.getId());

        // Assert that the actual list of members matches the expected list
        assertEquals(expectedMembers, gotGroup.getMembers());
        assertTrue(gotGroup.getMembers().contains(789L));
        assertTrue(gotGroup.getMembers().contains(790L));
        assertTrue(gotGroup.getMembers().contains(testUser.getId()));
    }


    @Test
    public void addUserToGroup_inValidGroupId_throwsException() {

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setId(2L);
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        userRepository.save(testUser);
        userRepository.flush();

        assertThrows(RuntimeException.class, () -> groupService.addUserToGroup(2L, 1L));

    }

    @Test
    public void addUserToGroup_inValidUserId_throwsException() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(790L);
        testGroup.setMembers(new ArrayList<>(initialMembers));
        testGroup = groupRepository.save(testGroup);
        groupRepository.flush();

        Long GroupID = testGroup.getId();

        assertThrows(ResponseStatusException.class, () -> groupService.addUserToGroup(2L, GroupID));

    }


    //  test deleteUserFromGroup method //
    @Test
    public void deleteUserFromGroup_validInputs_success() {


        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser = userRepository.save(testUser);
        userRepository.flush();

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(testUser.getId());
        testGroup.setMembers(new ArrayList<>(initialMembers));
        testGroup = groupRepository.save(testGroup);
        groupRepository.flush();

        // Call the method under test
        Group gotGroup = groupService.deleteUserFromGroup(testUser.getId(), testGroup.getId());

        List<Long> expectedMembers = new ArrayList<>(initialMembers);
        expectedMembers.remove(testUser.getId());

        // Assert that the actual list of members matches the expected list
        assertEquals(expectedMembers, gotGroup.getMembers());
        assertTrue(gotGroup.getMembers().contains(789L));
        assertFalse(gotGroup.getMembers().contains(testUser.getId()));
    }


    @Test
    public void deleteUserFromGroup_inValidUserId_throwsException() {

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        initialMembers.add(790L);
        testGroup.setMembers(new ArrayList<>(initialMembers));

        testGroup = groupRepository.save(testGroup);
        groupRepository.flush();

        Long GroupID = testGroup.getId();

        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(3L ,GroupID));

    }


    @Test
    public void deleteUserFromGroup_inValidGroupId_throwsException() {

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser = userRepository.save(testUser);
        userRepository.flush();

        Long UserID = testUser.getId();


        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(UserID, 790L));

    }


    @Test
    public void deleteUserFromGroup_invalidInputUserNotMemberOfGroup_throwsException() {


        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("username");
        testUser.setEmail("email.email@email.com");
        testUser.setToken(UUID.randomUUID().toString());
        Date creationDate = new Date();
        testUser.setCreationDate(creationDate);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser = userRepository.save(testUser);
        userRepository.flush();

        Long UserID = testUser.getId();

        Group testGroup = new Group();
        testGroup.setName("name");
        List<Long> initialMembers = new ArrayList<>();
        initialMembers.add(789L);
        testGroup.setMembers(new ArrayList<>(initialMembers));
        testGroup = groupRepository.save(testGroup);
        groupRepository.flush();

        Long GroupID = testGroup.getId();


        // Call the method under test
        assertThrows(RuntimeException.class, () -> groupService.deleteUserFromGroup(UserID, GroupID));
    }
}
