package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class ShoppingListPostDTO {

  private Long id;

  private String ingredient;

  public void setIngredient(String ingredient){
    this.ingredient = ingredient;
  }

  public String getIngredient(){
    return ingredient;
  }

  public void setId(Long id){
    this.id = id;
  }

  public Long getId(){
    return id;
  }
}
