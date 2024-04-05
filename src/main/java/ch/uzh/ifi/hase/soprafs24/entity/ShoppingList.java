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

  @Column(nullable = false)
  private String token;

  private List<String> items = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }

}
