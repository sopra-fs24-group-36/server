package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import ch.uzh.ifi.hase.soprafs24.constant.CalendarStatus;
import java.util.Date;

@Entity
@Table(name = "date_recipes")
public class DateRecipe {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

  @Temporal(TemporalType.DATE)
  private Date date;

  private Long recipeID;

  private CalendarStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public CalendarStatus getStatus() {
        return status;
    }

    public void setStatus(CalendarStatus status) {
        this.status = status;
    }
}
