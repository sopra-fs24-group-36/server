package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;
import ch.uzh.ifi.hase.soprafs24.constant.RecipeTags;

public class RecipePutDTO {

  private String title;

  private String shortDescription;

  private String link;

  private String cookingTime;

  private List<String> amounts = new ArrayList<>();

  private List<String> ingredients = new ArrayList<>();

  private List<String> instructions = new ArrayList<>();

  private String image;

  private List<RecipeTags> tags = new ArrayList<>(3);

  private List<Long> groups = new ArrayList<>();

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
    return groups;
  }

  public void setGroups(List<Long> groups) {
    this.groups = groups;
  }
}