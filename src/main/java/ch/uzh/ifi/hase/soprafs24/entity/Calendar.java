package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "CALENDAR")
public class Calendar implements Serializable {
  private static final long serialVersionUID = 1L;

  //create unique ID
  @Id
  @GeneratedValue
  private Long id;

  @ElementCollection
  private List<DateRecipe> dateRecipes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<DateRecipe> getDateRecipes() {
    return dateRecipes;
  }

  public void setDateRecipes(List<DateRecipe> dateRecipes) {
    this.dateRecipes = dateRecipes;
  }
}
