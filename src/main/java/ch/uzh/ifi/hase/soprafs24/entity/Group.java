package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {
  private static final long serialVersionUID = 1L;

  //create unique ID
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ElementCollection
  @CollectionTable(name = "members")
  private List<Long> members;

  //save the personal cookbook id to the group
  @OneToOne
  private Cookbook cookbook;

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

}
