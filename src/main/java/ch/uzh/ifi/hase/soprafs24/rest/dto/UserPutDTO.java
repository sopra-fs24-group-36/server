package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPutDTO {

    private String name;

    private String username;

    private String email;

    private String profilePicture;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email; }


    public String getProfilePicture() {return profilePicture;}

    public void setProfilePicture(String profilePicture) {this.profilePicture = profilePicture; }
}
