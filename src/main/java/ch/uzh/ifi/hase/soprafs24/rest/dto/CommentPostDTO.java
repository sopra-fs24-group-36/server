package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class CommentPostDTO {

  private String text;

  private Long userID;



  public String getText() {return text;}

  public void setText(String text) {
        this.text = text;
    }

  public Long getUserID() {
        return userID;
    }
  public void setUserID(Long userID) {
        this.userID = userID;
    }


}
