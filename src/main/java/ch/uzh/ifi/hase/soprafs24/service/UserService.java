package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

  //create a new user and save it to the database
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


    //log in the user, hence set the Status to ONLINE
    public User logIn (User userLogin) {

        checkIfUserNotExists(userLogin);

        User user = userRepository.findByUsername(userLogin.getUsername());

        user.setStatus(UserStatus.ONLINE);
        userRepository.flush();

        log.debug("Logged in User: {}", user);

        return user;
    }


    public void logOut (Long userId) {

      User user = userRepository.findById(userId).orElse(null);

      if (user == null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "log out user failed because user does not exist");
      }

      user.setStatus(UserStatus.OFFLINE);
      userRepository.flush();

      log.debug("Logged out User: {}", user);
    }




    //throw an error if the user does not already exist (if username or password or both provided is wrong)
    private void checkIfUserNotExists(User userToBeLoggedIn) {
        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());
        User userByPassword = userRepository.findByPassword(userToBeLoggedIn.getPassword());
        User userByUsernameAndPassword = userRepository.findByUsernameAndPassword(userToBeLoggedIn.getUsername(), userToBeLoggedIn.getPassword());

        String baseErrorMessage = "The %s provided %s not correct. Therefore, the user could not be logged in!";
        if (userByUsername == null && userByPassword == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username and the password", "are"));
        }
        else if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByPassword == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "password", "is"));
        }
        else if (userByUsernameAndPassword == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username or the password", "is"));
        }
    }



    //throw an error if the user already exists, a user already exists if the email or username provided or both is not unique
  private void checkIfUserExists(User userToBeCreated) {
      User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
      User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

      String baseErrorMessage = "The %s provided %s not correct. Therefore, the user could not be created!";
      if (userByUsername != null && userByEmail != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username and the email", "are"));
      } else if (userByUsername != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
      } else if (userByEmail != null) {
          throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "email", "is"));
      }
    }


  //public List<User> getUsers() {return this.userRepository.findAll();}


}
