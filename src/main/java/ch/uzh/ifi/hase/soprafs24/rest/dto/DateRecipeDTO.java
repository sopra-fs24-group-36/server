package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Date;

import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;

public class DateRecipeDTO {

  private Date date;

  private Long recipeID;

  private CalendarStatus status;

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

  public CalendarStatus getStatus() {
    return status;
  }

  public void setStatus(CalendarStatus status) {
    this.status = status;
  }
}
