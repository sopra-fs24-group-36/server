package ch.uzh.ifi.hase.soprafs24.rest.dto;


public class VoteRequestDTO {

  private Double vote;

  private Integer count;


  public Double getVote() {return vote;}

  public void setVote(Double vote) {
    this.vote = vote;
  }

  public Integer getCount() {return count;}

  public void setCount(Integer count) {
        this.count = count;
    }

}
