package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class CommentDTO {

  private Long id;

  private String text;

  private String username;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {return text;}

  public void setText(String text) {
        this.text = text;
    }

  public String getUsername() {
        return username;
    }
  public void setUsername(String username) {
        this.username = username;
    }

}
