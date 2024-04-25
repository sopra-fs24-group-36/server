package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "COOKBOOK")
public class Cookbook implements Serializable {
    private static final long serialVersionUID = 1L;

    //create unique ID
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private CookbookStatus status;

    @ElementCollection
    @Column(name = "recipes")
    private List<Long> recipes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CookbookStatus getStatus() {
        return status;
    }

    public void setStatus(CookbookStatus status) {
        this.status = status;
    }

    public List<Long> getRecipes(){
        return recipes;
    }

    public void setRecipes(List<Long> recipes){
        this.recipes = recipes;
    }

}
