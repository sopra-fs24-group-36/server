package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.CookbookStatus;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;

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

}
