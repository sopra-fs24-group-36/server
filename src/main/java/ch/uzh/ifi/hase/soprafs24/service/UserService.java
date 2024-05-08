package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Calendar;
import ch.uzh.ifi.hase.soprafs24.entity.Cookbook;
import ch.uzh.ifi.hase.soprafs24.entity.Group;
import ch.uzh.ifi.hase.soprafs24.entity.ShoppingList;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.GroupRepository;
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

  private final GroupRepository groupRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository, GroupRepository groupRepository) {
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
  }

  //create a new user and save it to the database
  public User createUser(User newUser) {
      newUser.setToken(UUID.randomUUID().toString());
      newUser.setStatus(UserStatus.ONLINE);
      Date creationDate = new Date();
      newUser.setCreationDate(creationDate);
      checkIfUserExists(newUser);

      if (newUser.getEmail().trim().isEmpty() || newUser.getUsername().trim().isEmpty() || newUser.getPassword().trim().isEmpty()) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameters need to have at least length 1");
      }

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


    public void saveCookbook (User user, Cookbook cookbook) {
        user.setCookbook(cookbook);
    }

    public void saveShoppingList(User user, ShoppingList shoppingList){
        user.setShoppingList(shoppingList);
    }

    public void saveCalendar(User user, Calendar calendar){
        user.setCalendar(calendar);
    }

    public User getTheUser (Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID was not found");
        }

        log.debug("Got the User: {}", user);

        return user;
    }

    public void updateTheUser (Long userId, User userUpdate) {

      //get user by id and check if they exist
        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "edit user profile failed as userId not found");
        }

        //check if email has been updated
        if (userUpdate.getEmail() != null) {
            //check if new email is unique
            User userByEmail = userRepository.findByEmail(userUpdate.getEmail());
            if (userByEmail == null) {

                if (userUpdate.getEmail().trim().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameters need to have at least length 1");
                }

                user.setEmail(userUpdate.getEmail());
                userRepository.flush();
                log.debug("Email changed for User: {}", user);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new email not unique, profile could not be updated");
            }
        }

        //check if username has been updated
        if (userUpdate.getUsername() != null) {
            //check if new username is unique
            User userByUsername = userRepository.findByUsername(userUpdate.getUsername());
            if (userByUsername == null) {

                if (userUpdate.getUsername().trim().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameters need to have at least length 1");
                }
                user.setUsername(userUpdate.getUsername());
                userRepository.flush();
                log.debug("Username changed for User: {}", user);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new username not unique, profile could not be updated");
            }
        }

        //check if name has been updated
        if (userUpdate.getName() != null) {

            if (userUpdate.getName().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parameters need to have at least length 1");
            }

            user.setName(userUpdate.getName());
            userRepository.flush();
            log.debug("Name changed for User: {}", user);
        }

        //check if profilePicture has been updated
        if (userUpdate.getProfilePicture() != null) {
            user.setProfilePicture(userUpdate.getProfilePicture());
            userRepository.flush();
            log.debug("ProfilePicture changed for User: {}", user);
        }

    }


    public User userDeclinesInvitation (Long userId, Long groupId) {

        //get the invitations from user, throw error if null
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        //remove the groupId of invitations from user
        List<Long> invitations = user.getInvitations();
        if (invitations == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found");
        }

        if (invitations.contains(groupId)) {
            invitations.remove(groupId);
            user.setInvitations(invitations);
            userRepository.save(user);
            userRepository.flush();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found");
        }

        return user;

    }


    public User userAcceptsInvitation (Long userId, Long groupId) {

        //get the user which accepted the invitation
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        //remove the invitation of groupId from the user
        //add the group to the list of groups the user is a member of
        List<Long> invitations = user.getInvitations();
        List<Long> userGroups = user.getGroups();
        //check that invitations exist
        if (invitations == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found");
        }
        if (invitations.contains(groupId)) {
            invitations.remove(groupId);
            user.setInvitations(invitations);
            userGroups.add(groupId);
            user.setGroups(userGroups);
            userRepository.save(user);
            userRepository.flush();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found");
        }

        //check that group exists
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found");
        }

        //add user as member of group
        List<Long> members = group.getMembers();
        if (members == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Members are null");
        }
        if (!members.contains(userId)) {
            members.add(userId);
            group.setMembers(members);
            groupRepository.save(group);
            groupRepository.flush();
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already in group");
        }
        return user;
    }




    //throw an error if the user does not already exist (if username or password or both provided is wrong)
    private void checkIfUserNotExists(User userToBeLoggedIn) {
        User userByUsernameAndPassword = userRepository.findByUsernameAndPassword(userToBeLoggedIn.getUsername(), userToBeLoggedIn.getPassword());

        String baseErrorMessage = "The %s provided %s not correct. Therefore, the user could not be logged in!";
        if (userByUsernameAndPassword == null) {
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


}
