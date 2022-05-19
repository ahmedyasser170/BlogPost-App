package com.example.BlogPostApp.model;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String body;
    private Date dateCreated;
    private boolean premium;

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @ManyToOne
    private User creator;


    public Post() {
    }

    public Post(String title, String body, Date dateCreated, boolean premium, User creator) {
        this.title = title;
        this.body = body;
        this.dateCreated = dateCreated;
        this.premium = premium;
        this.creator = creator;
    }


}
