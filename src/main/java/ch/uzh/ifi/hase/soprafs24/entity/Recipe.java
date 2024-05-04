package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "RECIPE")
public class Recipe implements Serializable {
  private static final long serialVersionUID = 1L;

  //create unique ID
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "shortDescription")
  private String shortDescription;

  @Lob
  @Column(name = "link")
  private String link;

  @Column(name = "cookingTime")
  private String cookingTime;

  @ElementCollection
  @Column(name = "amounts")
  private List<String> amounts;

  @ElementCollection
  @Column(name = "ingredients")
  private List<String> ingredients;

  @ElementCollection
  @Column(name = "instructions")
  private List<String> instructions;

  @Lob
  @Column(name = "image")
  private String image;

  @ElementCollection
  @Column(name = "tags")
  private List<RecipeTags> tags;

  @ElementCollection
  @Column(name = "groupIDs")
  private List<Long> groupIDs;

  @ElementCollection
  @Column(name = "commentIDs")
  private List<Long> commentIDs;

  private Long authorID;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getCookingTime() {
    return cookingTime;
  }

  public void setCookingTime(String cookingTime) {
    this.cookingTime = cookingTime;
  }

  public List<String> getAmounts() {
    return amounts;
  }

  public void setAmounts(List<String> amounts) {
    this.amounts = amounts;
  }

  public List<String> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<String> ingredients) {
    this.ingredients = ingredients;
  }

  public List<String> getInstructions() {
    return instructions;
  }

  public void setInstructions(List<String> instructions) {
    this.instructions = instructions;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public List<RecipeTags> getTags() {
    return tags;
  }

  public void setTags(List<RecipeTags> tags) {
    this.tags = tags;
  }

  public List<Long> getGroups() {
    return groupIDs;
  }

  public void setGroups(List<Long> groupIDs) {
    this.groupIDs = groupIDs;
  }
  public Long getAuthorID() {
    return authorID;
  }

  public void setAuthorID(Long authorID) {
    this.authorID = authorID;
  }

  public List<Long> getComments(){return commentIDs;}
  public void setComments(List<Long> commentIDs){this.commentIDs = commentIDs;}
}
