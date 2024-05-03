package ch.uzh.ifi.hase.soprafs24.entity;

import java.util.Date;

import javax.persistence.Lob;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;

public class CalendarOutput {

  private Long recipeID;

  private String recipeTitle;

  @Lob
  private String recipeImage;
  
  private Date date;

  private CalendarStatus status;

  public Long getRecipeID() {
    return recipeID;
  }

  public void setRecipeID(Long recipeID) {
    this.recipeID = recipeID;
  }

  public String getRecipeTitle() {
    return recipeTitle;
  }

  public void setRecipeTitle(String recipeTitle) {
    this.recipeTitle = recipeTitle;
  }

  public String getRecipeImage() {
    return recipeImage;
  }

  public void setRecipeImage(String recipeImage) {
    this.recipeImage = recipeImage;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public CalendarStatus getStatus() {
    return status;
  }

  public void setStatus(CalendarStatus status) {
    this.status = status;
  }
}
