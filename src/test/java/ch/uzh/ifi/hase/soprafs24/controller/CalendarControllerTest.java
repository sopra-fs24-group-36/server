package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarOutput;
import ch.uzh.ifi.hase.soprafs24.entity.CalendarRequest;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.CalendarRepository;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.CalendarDeleteDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.DateRecipeDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.CalendarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@WebMvcTest(CalendarController.class)
public class CalendarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalendarService calendarService;

    @MockBean
    private CalendarRepository calendarRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private GroupRepository groupRepository;

    @Test
    public void addRecipeToCalendar_validInput_success() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setUsername("testUsername");
      user.setEmail("testMail");
      user.setPassword("testPassword");
      user.setName("testName");
      user.setCreationDate(new Date());
      user.setStatus(UserStatus.OFFLINE);
      user.setProfilePicture("testPicture");
      user.setCalendar(new Calendar());

      DateRecipeDTO dateRecipeDTO = new DateRecipeDTO();
      dateRecipeDTO.setRecipeID(10L);
      dateRecipeDTO.setDate(new Date());
      dateRecipeDTO.setStatus(CalendarStatus.BREAKFAST);

      // Mocking repository and service
      Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
      Mockito.when(calendarService.addRecipeToUserCalendar(Mockito.any(User.class), Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(CalendarStatus.class))).thenReturn(new Calendar());

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/calendars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dateRecipeDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk());

      // Verify interactions
      verify(calendarService).addRecipeToUserCalendar(any(User.class), eq(10L), any(Date.class), eq(CalendarStatus.BREAKFAST));
    }


    @Test
    public void addRecipeToCalendar_invalidInput_throwsException() throws Exception {
      
      // given
      DateRecipeDTO dateRecipeDTO = new DateRecipeDTO();
      dateRecipeDTO.setRecipeID(10L);
      dateRecipeDTO.setDate(new Date());
      dateRecipeDTO.setStatus(CalendarStatus.BREAKFAST);

      // Mocking repository and service
      Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
      Mockito.when(calendarService.addRecipeToUserCalendar(Mockito.any(User.class), Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(CalendarStatus.class))).thenReturn(new Calendar());

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.post("/users/1/calendars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dateRecipeDTO)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void addRecipeToGroupCalendar_validInput_success() throws Exception {
      // given
      Group group = new Group();
      group.setId(1L);
      group.setName("testName");      

      DateRecipeDTO dateRecipeDTO = new DateRecipeDTO();
      dateRecipeDTO.setRecipeID(10L);
      dateRecipeDTO.setDate(new Date());
      dateRecipeDTO.setStatus(CalendarStatus.BREAKFAST);

      // Mocking repository and service
      Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
      Mockito.when(calendarService.addRecipeToGroupCalendar(Mockito.any(Group.class), Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(CalendarStatus.class))).thenReturn(new Calendar());

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.post("/groups/" + group.getId() + "/calendars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dateRecipeDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk());

      // Verify interactions
      verify(calendarService).addRecipeToGroupCalendar(any(Group.class), eq(10L), any(Date.class), eq(CalendarStatus.BREAKFAST));
    }

    @Test
    public void addRecipeToGroupCalendar_invalidInput_throwsException() throws Exception {
      
      // given
      DateRecipeDTO dateRecipeDTO = new DateRecipeDTO();
      dateRecipeDTO.setRecipeID(10L);
      dateRecipeDTO.setDate(new Date());
      dateRecipeDTO.setStatus(CalendarStatus.BREAKFAST);

      // Mocking repository and service
      Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
      Mockito.when(calendarService.addRecipeToGroupCalendar(Mockito.any(Group.class), Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(CalendarStatus.class))).thenReturn(new Calendar());

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.post("/groups/1/calendars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(dateRecipeDTO)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void removeRecipeFromUserCalendar_validInput_success() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setUsername("testUsername");
      user.setEmail("testMail");
      user.setPassword("testPassword");
      user.setName("testName");
      user.setCreationDate(new Date());
      user.setStatus(UserStatus.OFFLINE);
      user.setProfilePicture("testPicture");
      user.setCalendar(new Calendar());

      CalendarDeleteDTO calendarDeleteDTO = new CalendarDeleteDTO();
      calendarDeleteDTO.setEventId(10L);

      // Mocking repository and service
      Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
      Mockito.when(calendarService.removeRecipeFromUserCalendar(Mockito.any(User.class), Mockito.anyLong())).thenReturn(new Calendar());

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId() + "/calendars/" + calendarDeleteDTO.getEventId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(calendarDeleteDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk());

      // Verify interactions
      verify(calendarService).removeRecipeFromUserCalendar(any(User.class), eq(10L));
    }

    @Test
    public void removeRecipeFromUserCalendar_invalidInput_throwsException() throws Exception {
      // given
      CalendarDeleteDTO calendarDeleteDTO = new CalendarDeleteDTO();
      calendarDeleteDTO.setEventId(10L);

      // Mocking repository and service
      Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
      Mockito.when(calendarService.removeRecipeFromUserCalendar(Mockito.any(User.class), Mockito.anyLong())).thenReturn(new Calendar());
      
      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/calendars/" + calendarDeleteDTO.getEventId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(calendarDeleteDTO)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void removeRecipeFromGroupCalendar_validInput_success() throws Exception {
      // given
      Group group = new Group();
      group.setId(1L);
      group.setName("testName");

      CalendarDeleteDTO calendarDeleteDTO = new CalendarDeleteDTO();
      calendarDeleteDTO.setEventId(10L);

      // Mocking repository and service
      Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
      Mockito.when(calendarService.removeRecipeFromGroupCalendar(Mockito.any(Group.class), Mockito.anyLong())).thenReturn(new Calendar());

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.delete("/groups/" + group.getId() + "/calendars/" + calendarDeleteDTO.getEventId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(calendarDeleteDTO)))
            .andExpect(MockMvcResultMatchers.status().isOk());

      // Verify interactions
      verify(calendarService).removeRecipeFromGroupCalendar(any(Group.class), eq(10L));
    }

    @Test
    public void removeRecipeFromGroupCalendar_invalidInput_throwsException() throws Exception {
      // given
      CalendarDeleteDTO calendarDeleteDTO = new CalendarDeleteDTO();
      calendarDeleteDTO.setEventId(10L);

      // Mocking repository and service
      Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
      Mockito.when(calendarService.removeRecipeFromGroupCalendar(Mockito.any(Group.class), Mockito.anyLong())).thenReturn(new Calendar());
      
      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.delete("/groups/1/calendars/" + calendarDeleteDTO.getEventId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(calendarDeleteDTO)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserCalendar_validInput_success() throws Exception {
      // given
      User user = new User();
      user.setId(1L);
      user.setUsername("testUsername");
      user.setEmail("testMail");
      user.setPassword("testPassword");
      user.setName("testName");
      user.setCreationDate(new Date());
      user.setStatus(UserStatus.OFFLINE);
      user.setProfilePicture("testPicture");
      user.setCalendar(new Calendar());

      CalendarOutput calendarOutput = new CalendarOutput();
      calendarOutput.setEventId(10L);

      ArrayList<CalendarOutput> calendarOutputs = new ArrayList<>();
      calendarOutputs.add(calendarOutput);

      // Mocking repository and service
      Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
      Mockito.when(calendarService.getUserCalendar(Mockito.any(User.class))).thenReturn(calendarOutputs);

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/calendars"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$[0].eventId").value(10L));

      // Verify interactions
      verify(calendarService).getUserCalendar(any(User.class));
    }

    @Test
    public void getUserCalendar_invalidInput_throwsException() throws Exception {
      // given
      // Mocking repository and service
      Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
      Mockito.when(calendarService.getUserCalendar(Mockito.any(User.class))).thenReturn(new ArrayList<CalendarOutput>());
      
      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.get("/users/1/calendars"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getGroupCalendar_validInput_success() throws Exception {
      // given
      Group group = new Group();
      group.setId(1L);
      group.setName("testName");
      group.setCalendar(new Calendar());

      CalendarOutput calendarOutput = new CalendarOutput();
      calendarOutput.setEventId(10L);

      ArrayList<CalendarOutput> calendarOutputs = new ArrayList<>();
      calendarOutputs.add(calendarOutput);

      // Mocking repository and service
      Mockito.when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
      Mockito.when(calendarService.getGroupCalendar(Mockito.any(Group.class))).thenReturn(calendarOutputs);

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.get("/groups/" + group.getId() + "/calendars"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$[0].eventId").value(10L));

      // Verify interactions
      verify(calendarService).getGroupCalendar(any(Group.class));
    }

    @Test
    public void getGroupCalendar_invalidInput_throwsException() throws Exception {
      
      // given
      CalendarOutput calendarOutput = new CalendarOutput();
      calendarOutput.setEventId(10L);

      ArrayList<CalendarOutput> calendarOutputs = new ArrayList<>();
      calendarOutputs.add(calendarOutput);

      // Mocking repository and service
      Mockito.when(groupRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
      Mockito.when(calendarService.getGroupCalendar(Mockito.any(Group.class))).thenReturn(calendarOutputs);

      // when/then -> do the request + validate the result
      mockMvc.perform(MockMvcRequestBuilders.get("/groups/1/calendars"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
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
    