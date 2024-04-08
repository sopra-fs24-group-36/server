package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupDTO {
  
  private Long id;

  private String name;

  private List<Long> members = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Long> getMembers() {
    return members;
  }

  public void setMembers(List<Long> members) {
    this.members = members;
  }  
}
