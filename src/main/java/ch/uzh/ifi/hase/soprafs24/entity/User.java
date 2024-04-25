package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "USERS")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column (nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    //name is optional, hence can be null
    @Column
    private String name;

    @Column (nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(nullable = false)
    private UserStatus status;

    //optional, hence can be null
    @Lob
    @Column(name="profilePicture")
    private String profilePicture;

    @Column(nullable = false, unique = true)
    private String token;

    //save the personal cookbook id to the user
    @OneToOne
    private Cookbook cookbook;

    @OneToOne
    private ShoppingList shoppingList;

    @ElementCollection
    @Column(name = "invitations")
    private List<Long> invitations;

    @ElementCollection
    @Column(name = "usergroups")
    private List<Long> groups;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate () { return creationDate; }

    public void setCreationDate (Date creationDate) {this.creationDate = creationDate; }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Cookbook getCookbook() {return cookbook; }

    public void setCookbook(Cookbook cookbook) {this.cookbook = cookbook; }

    public List<Long> getInvitations(){return invitations;}

    public void setInvitations(List<Long> invitations){this.invitations = invitations;}

    public ShoppingList getShoppingList() {return shoppingList; }

    public void setShoppingList(ShoppingList shoppingList) {this.shoppingList = shoppingList; } 

    public List<Long> getGroups(){return groups;}

    public void setGroups(List<Long> groups){this.groups = groups;}
}
