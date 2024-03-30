package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */


public class DTOMapperTest {

    //test the UserPostDTOtoUser with the createUser method
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("name");
        userPostDTO.setUsername("username");
        userPostDTO.setEmail("email.email@email.com");
        userPostDTO.setPassword("password");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getName(), user.getName());
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getEmail(), user.getEmail());
        assertEquals(userPostDTO.getPassword(), user.getPassword());
    }

    //test the UserPostDTOtoUser with the createUser method
    @Test
    public void testCreateUser_fromUser_toUserDTO_success() {
        // create UserPostDTO
        User user = new User();
        user.setName("name");
        user.setUsername("username");
        user.setEmail("email.email@email.com");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        Date creationDate = new Date();
        user.setCreationDate(creationDate);

        // MAP -> Create user
        UserDTO userDTO = DTOMapper.INSTANCE.convertEntityToUserDTO(user);

        // check content
        assertEquals(user.getName(), userDTO.getName());
        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertNotNull(user.getCreationDate());
        assertEquals(user.getStatus(), userDTO.getStatus());
    }
}