package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupPostDTO {
 //need some help here because it doesn't make a lot of sense somehow

  private String name;

  private List<Long> members = new ArrayList<>();

  private String image;

  public void setName(String name){
    this.name = name;
  }

  public void setMembers(List<Long> members){
    this.members = members;
  }

  public String getName(){
    return name;
  }

  public List<Long> getMembers(){
    return members;
  }

  public String getImage(){return image;}

  public void setImage(String image){this.image = image;}
}
