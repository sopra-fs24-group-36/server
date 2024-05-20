package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupPostDTO {

  private String name;

  private List<Long> members = new ArrayList<>();

  private List<String> membersNames = new ArrayList<>();

  private String image;

  private Long creator;

  public void setName(String name){
    this.name = name;
  }

  public void setMembersNames(List<String> membersNames){
    this.membersNames = membersNames;
  }

  public void setMembers(List<Long> members){
    this.members = members;
  }

  public String getName(){
    return name;
  }

  public List<String> getMembersNames(){
    return membersNames;
  }

  public List<Long> getMembers(){
    return members;
  }
  public String getImage(){return image;}

  public void setImage(String image){this.image = image;}

  public Long getCreator(){return creator;}

  public void setCreator(Long creator){this.creator = creator;}
}
