package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SHOPPINGLIST")
public class ShoppingList implements Serializable {
  private static final long serialVersionUID = 1L;

  //create unique ID
  @Id
  @GeneratedValue
  private Long id;

  @ElementCollection
  @CollectionTable(name = "items")
  private List<String> items;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

}
