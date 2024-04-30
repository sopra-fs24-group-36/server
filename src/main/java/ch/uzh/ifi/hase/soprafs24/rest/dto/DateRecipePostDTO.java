package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;

public class DateRecipePostDTO {

  private Date date;

  private Long recipeID;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Long getRecipeID() {
    return recipeID;
  }

  public void setRecipeID(Long recipeID) {
    this.recipeID = recipeID;
  }
}
