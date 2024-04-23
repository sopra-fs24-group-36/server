package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.service.CookbookService;
import ch.uzh.ifi.hase.soprafs24.service.ShoppingListService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;


import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private CookbookService cookbookService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private GroupRepository groupRepository;

  @MockBean
  private ShoppingListService shoppingListService;


  //  test the /users POST Mapping  //
  @Test
  public void createUser_validInput_userCreated() throws Exception {
      //all fields required by the USER
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

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setPassword("password");
      userPostDTO.setName("name");
      userPostDTO.setUsername("testUsername");
      userPostDTO.setEmail("email.email@email.com");

      given(userService.createUser(Mockito.any())).willReturn(user);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

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
    public void createUser_inValidInput_userCreated() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("TestPassword");
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("email.email@email.com");
        userPostDTO.setName("name");

        //mocks the user Service, this defines what createUser should return if it is called
        given(userService.createUser(Mockito.any()))
                .willThrow(new IllegalStateException("add User failed because username already exists"));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        //perform request twice, which should result in the conflict 409
        mockMvc.perform(postRequest);
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }


    //  test the /users/login POST Mapping  //
    @Test
    public void logInUser_ValidInput() throws Exception {

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

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("password");
        userPostDTO.setUsername("username");

         given(userService.logIn(Mockito.any())).willReturn(user);

         MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.creationDate", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$")))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test
    public void logInUser_InValidInput() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("password");
        userPostDTO.setUsername("username");

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be logged in"))
                .when(userService).logIn(Mockito.any());


        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }


    //  test the /users/logout/{userId} POST Mapping  //
    @Test
    public void logOutUser_ValidInput() throws Exception {

        MockHttpServletRequestBuilder postRequest = post("/users/logout/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void logOutUser_InValidInput() throws Exception {

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be logged out"))
                .when(userService).logOut(Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/users/logout/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }


    //  test the /users/{userId} GET Mapping  //
    @Test
    public void getUser_validInput() throws Exception {
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


        given(userService.getTheUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.creationDate", matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}\\+\\d{2}:\\d{2}$")))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }


    @Test
    public void getUser_InvalidInput() throws Exception {


        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID was not found"))
                .when(userService).getTheUser(Mockito.any());


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isBadRequest());
    }


    //  test the /users/{userId} PUT Mapping  //
    @Test
    public void updateUser_ValidInput() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("username");
        userPutDTO.setName("name");
        userPutDTO.setProfilePicture("profilePic");
        userPutDTO.setEmail("email");

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }


    @Test
    public void updateUser_inValidInput() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("username");
        userPutDTO.setName("name");
        userPutDTO.setProfilePicture("profilePic");
        userPutDTO.setEmail("email");

        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot be updated"))
                .when(userService).updateTheUser(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void acceptInvitation_validInput() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setGroups(new ArrayList<>());

        Group group = new Group();
        group.setId(2L);
        group.setName("test");
        group.setMembers(new ArrayList<>());

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        user.setInvitations(invitations);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));

        MockHttpServletRequestBuilder postRequest = post("/users/1/accept/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void acceptInvitation_invalidInput_throwsException() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setGroups(new ArrayList<>());

        Group group = new Group();
        group.setId(2L);
        group.setName("test");
        group.setMembers(new ArrayList<>());

        List<Long> invitations = new ArrayList<>();
        user.setInvitations(invitations);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));

        MockHttpServletRequestBuilder postRequest = post("/users/1/accept/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void acceptInvitation_invalidInput_conflict() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setGroups(new ArrayList<>());

        Group group = new Group();
        group.setId(2L);
        group.setName("test");
        List<Long> members = new ArrayList<>();
        members.add(1L);
        group.setMembers(members);

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        user.setInvitations(invitations);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));

        MockHttpServletRequestBuilder postRequest = post("/users/1/accept/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void acceptInvitation_invalidInput_noUser() throws Exception{

        Group group = new Group();
        group.setId(2L);
        group.setName("test");
        group.setMembers(new ArrayList<>());


        given(userRepository.findById(Mockito.anyLong())).willReturn(null);
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));

        MockHttpServletRequestBuilder postRequest = post("/users/1/accept/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void acceptInvitation_invalidInput_noGroup() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setGroups(new ArrayList<>());

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        user.setInvitations(invitations);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder postRequest = post("/users/1/accept/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void declineInvitation_validInput() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setGroups(new ArrayList<>());

        List<Long> invitations = new ArrayList<>();
        invitations.add(2L);
        user.setInvitations(invitations);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder postRequest = post("/users/1/deny/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void declineInvitation_invalidInput_UserNotFound() throws Exception{
        given(userRepository.findById(Mockito.anyLong())).willReturn(null);

        MockHttpServletRequestBuilder getRequest = post("/users/1/deny/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());        
    }

    @Test
    public void declineInvitation_invalidInput_throwsException() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        user.setGroups(new ArrayList<>());

        List<Long> invitations = new ArrayList<>();
        user.setInvitations(invitations);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        MockHttpServletRequestBuilder postRequest = post("/users/1/deny/2")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }


    @Test
    public void getAllInvitations_validInput() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        List<Long> list = new ArrayList<>();
        list.add(2L);
        user.setInvitations(list);

        Group group = new Group();
        group.setName("test");
        group.setImage("test");
        group.setId(2L);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));

        MockHttpServletRequestBuilder getRequest = get("/users/1/invitations")
        .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getAllInvitations_invalidInput_noUser() throws Exception{

        given(userRepository.findById(Mockito.anyLong())).willReturn(null);

        MockHttpServletRequestBuilder getRequest = get("/users/1/invitations")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllInvitations_invalidInput_GroupNotFound() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        List<Long> list = new ArrayList<>();
        list.add(2L);
        user.setInvitations(list);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        MockHttpServletRequestBuilder getRequest = get("/users/1/invitations")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserGroups_validInput() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setName("test");
        user.setPassword("test");
        user.setEmail("test");
        List<Long> groups = new ArrayList<>();
        groups.add(2L);
        user.setGroups(groups);

        Group group = new Group();
        group.setName("test");
        group.setImage("test");
        group.setId(2L);

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.of(group));

        MockHttpServletRequestBuilder getRequest = get("/users/1/groups")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
        }

        @Test
        public void getUserGroups_invalidInput_UserNotFound() throws Exception{

            given(userRepository.findById(Mockito.anyLong())).willReturn(null);
    
            MockHttpServletRequestBuilder getRequest = get("/users/1/groups")
                    .contentType(MediaType.APPLICATION_JSON);
    
            mockMvc.perform(getRequest)
                    .andExpect(status().isNotFound());
        }

        @Test
        public void getUserGroups_invalidInput_GroupNotFound() throws Exception{
            User user = new User();
            user.setId(1L);
            user.setUsername("test");
            user.setName("test");
            user.setPassword("test");
            user.setEmail("test");
            List<Long> groups = new ArrayList<>();
            groups.add(2L);
            user.setGroups(groups);
    

            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(groupRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
    
            MockHttpServletRequestBuilder getRequest = get("/users/1/groups")
                    .contentType(MediaType.APPLICATION_JSON);
    
            mockMvc.perform(getRequest)
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