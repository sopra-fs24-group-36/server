package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDTO {

  private List<String> items = new ArrayList<>();

  public void setItems(List<String> items){
    this.items = items;
  }

  public List<String> getItems(){
    return items;
  }
}
