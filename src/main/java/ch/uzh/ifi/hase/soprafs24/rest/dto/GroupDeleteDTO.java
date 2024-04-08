package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class GroupDeleteDTO {

  private Long userID;

  public void setUserID(Long userID){
    this.userID = userID;
  }

  public Long getUserID(){
    return userID;
  }
}
