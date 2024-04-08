package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ShoppingListPutDTO {
  
  private Long itemID;

  public void setId(Long itemID){
    this.itemID = itemID;
  }

  public Long getId(){
    return itemID;
  }
}
