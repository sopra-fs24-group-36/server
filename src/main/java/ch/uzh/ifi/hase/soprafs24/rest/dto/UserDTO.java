package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import java.util.Date;

public class UserDTO {

    private Long id;

    private String email;

    private String username;

    private String name;

    private Date creationDate;

    private UserStatus status;

    private String profilePicture;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate () {return creationDate; }

    public void setCreationDate (Date creationDate) {this.creationDate = creationDate; }

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {this.status = status;}

    public String getProfilePicture() {return profilePicture;}

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture; }

}
