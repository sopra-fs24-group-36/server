package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupPostDTO {
 //need some help here because it doesn't make a lot of sense somehow

  private String name;

  private List<Long> members = new ArrayList<>();
 
  private Long userID;

  private Long groupID;

  public void setName(String name){
    this.name = name;
  }

  public void setMembers(List<Long> members){
    this.members = members;
  }

  public void setUserID(Long userID){
    this.userID = userID;
  }

  public void setGroupID(Long groupID){
    this.groupID = groupID;
  }

  public String getName(){
    return name;
  }

  public List<Long> getMembers(){
    return members;
  }

  public Long getUserID(){
    return userID;
  }

  public Long getGroupID(){
    return groupID;
  }
}
