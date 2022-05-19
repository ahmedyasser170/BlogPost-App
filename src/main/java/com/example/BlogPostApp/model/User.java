package com.example.BlogPostApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Expose private Long id;
    @Expose private String name;
    @Expose private String userName;
    @JsonIgnore
    private String password;
    @Expose private String eMail;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Collection<Role> roles =new ArrayList<>();

    public User() {
    }

    public User(String name, String userName, String password, String eMail, Collection<Role> role) {
        this.name = name;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.roles = role;
    }


    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
