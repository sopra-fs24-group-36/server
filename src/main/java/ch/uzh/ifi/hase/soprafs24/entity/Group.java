package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {
  private static final long serialVersionUID = 1L;

  //create unique ID
  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String name;

  @ElementCollection
  @Column(name = "members")
  private List<Long> members;


  @ElementCollection
  @Column(name = "members_names")
  private List<String> membersNames;

  //save the personal cookbook id to the group
  @OneToOne
  private Cookbook cookbook;

  @OneToOne
  private ShoppingList shoppingList;

  @Lob
  private String image;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Long> getMembers() {
    return members;
  }

  public void setMembers(List<Long> members) {
    this.members = members;
  }

  public Cookbook getCookbook() {return cookbook; }

  public void setCookbook(Cookbook cookbook) {this.cookbook = cookbook; }

  public String getImage(){return image;}

  public void setImage(String image){this.image = image;}

  public ShoppingList getShoppingList() {return shoppingList; }

  public void setShoppingList(ShoppingList shoppingList) {this.shoppingList = shoppingList; }

  public List<String> getMembersNames(){return membersNames;}

  public void setMembersNames(List<String> membersNames){this.membersNames = membersNames;}

}
