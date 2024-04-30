package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.util.Date;

@Embeddable
public class DateRecipe {
  @Temporal(TemporalType.DATE)
  private Date date;

  @ManyToOne
  private Recipe recipe;

  public Date getDate() {
      return date;
  }

  public void setDate(Date date) {
      this.date = date;
  }

  public Recipe getRecipe() {
      return recipe;
  }

  public void setRecipe(Recipe recipe) {
      this.recipe = recipe;
  }
}
