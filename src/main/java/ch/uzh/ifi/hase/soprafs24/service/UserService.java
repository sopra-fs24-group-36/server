package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User createUser(User newUser) {
      newUser.setToken(UUID.randomUUID().toString());
      newUser.setStatus(UserStatus.ONLINE);
      Date creationDate = new Date();
      newUser.setCreationDate(creationDate);
      checkIfUserExists(newUser);

      newUser = userRepository.save(newUser);
      userRepository.flush();

      log.debug("Created Information for User: {}", newUser);
      return newUser;
    }

    //throw an error if the user already exists, an user already exists if the email or username provided or both is not unique
  private void checkIfUserExists(User userToBeCreated) {
      User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
      User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

      if (userByUsername != null && userByEmail != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT,
          "The username and email provided are not unique. Therefore, the user could not be created.");
      } else if (userByUsername != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT,
                  "The username provided is not unique. Therefore, the user could not be created.");
      } else if (userByEmail != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT,
                  "The email provided is not unique. Therefore, the user could not be created.");
      }
    }


  //public List<User> getUsers() {return this.userRepository.findAll();}


}
