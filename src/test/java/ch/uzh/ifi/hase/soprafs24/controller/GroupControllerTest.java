package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GroupPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.GroupService;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.digester.ArrayStack;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
public class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private CookbookService cookbookService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GroupRepository groupRepository;

    @MockBean
    private ShoppingListService shoppingListService;


    //  test for Post /groups mapping   //
    @Test
    public void createGroup_validInput_groupCreated() throws Exception {

        //the group that should be created
        Group group = new Group();
        group.setId(1L);
        group.setName("name");
        List<Long> m = new ArrayList<>();
        m.add(1L);
        group.setMembers(m);

        //information for the request
        GroupPostDTO groupPostDTO = new GroupPostDTO();
        groupPostDTO.setName("name");
        groupPostDTO.setCreator(1L);

        User user = new User();
        user.setId(1L);
        List<Long> l = new ArrayList<>();
        user.setGroups(l);

        Long userID = 1L;

        given(userRepository.findById(userID)).willReturn(Optional.of(user));
        given(groupService.createGroup(Mockito.any())).willReturn(group);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(group.getId().intValue())))
                .andExpect(jsonPath("$.name", is(group.getName())));
    }

    @Test
    public void createGroup_inValidInput_groupCreated() throws Exception {
        //fields for GROUPS
        List<Long> members = Arrays.asList(10L, 20L, 30L);

        //information for the request
        GroupPostDTO groupPostDTO = new GroupPostDTO();
        groupPostDTO.setMembers(members);
        groupPostDTO.setCreator(1L);

        User user = new User();
        user.setId(1L);
        List<Long> l = new ArrayList<>();
        user.setGroups(l);

        Long userID = 1L;

        given(userRepository.findById(userID)).willReturn(Optional.of(user));
        given(groupService.createGroup(Mockito.any()))
                .willThrow(new IllegalStateException("Creating a group failed because the details were incomplete"));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(groupPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }


    //  test for Post /groups/groupId/userId mapping   //
    @Test
    public void addUserToGroup_validInput_userAdded() throws Exception {

        //user we want to add and the credentials
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setName("name");
        user.setPassword("password");
        user.setEmail("email.email@email.com");
        Date creationDate = new Date();
        user.setCreationDate(creationDate);
        user.setStatus(UserStatus.ONLINE);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/groups/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.creationDate", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$")))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test
    public void addUserToGroup_inValidInput_userAdded() throws Exception {

        given(userRepository.findById(Mockito.any()))
                .willThrow(new IllegalStateException("User to add to the group not found"));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/groups/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }



    //  test for Delete /groups/groupId/userId mapping   //
    @Test
    public void deleteUserFromGroup_validInput_userDeleted() throws Exception {

        //user we want to add and the credentials
        User user = new User();
        user.setId(1L);
        user.setUsername("testUsername");
        user.setToken("1");
        user.setName("name");
        user.setPassword("password");
        user.setEmail("email.email@email.com");
        Date creationDate = new Date();
        user.setCreationDate(creationDate);
        user.setStatus(UserStatus.ONLINE);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/groups/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }


    @Test
    public void deleteUserFromGroup_inValidInput_userDeleted() throws Exception {

        given(userRepository.findById(Mockito.any()))
                .willThrow(new IllegalStateException("User to add to the group not found"));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/groups/1/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());
    }




    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }

}
